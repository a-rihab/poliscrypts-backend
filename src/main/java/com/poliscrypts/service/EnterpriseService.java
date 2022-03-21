package com.poliscrypts.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.poliscrypts.dto.EnterpriseDto;
import com.poliscrypts.exception.EntityNotFoundException;
import com.poliscrypts.model.Contact;
import com.poliscrypts.model.Enterprise;
import com.poliscrypts.repository.EnterpriseRepository;
import com.poliscrypts.util.PageContent;

@Service
public class EnterpriseService {

	@Autowired
	private EnterpriseRepository enterpriseRepository;

	@PersistenceContext
	private EntityManager entityManager;

	public EnterpriseDto createEnterprise(EnterpriseDto enterpriseDto) {

		Enterprise enterprise = mapDtoToEntity(enterpriseDto);

		return mapEntityToDto(enterpriseRepository.save(enterprise));
	}

	public EnterpriseDto updateEnterprise(Long id, EnterpriseDto enterpriseDto) {

		enterpriseRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Impossible to update entreprise with id " + id));

		Enterprise enterprise = mapDtoToEntity(enterpriseDto);

		enterprise.setId(id);

		return mapEntityToDto(enterpriseRepository.save(enterprise));
	}

	public PageContent<EnterpriseDto> getAllEnterprises(Integer page, Integer limit, String sort, String dir) {

		PageContent<EnterpriseDto> pageContent = new PageContent<EnterpriseDto>();

		if (limit == -1)
			limit = enterpriseRepository.findAll().size();

		Sort _sort = dir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sort).ascending()
				: Sort.by(sort).descending();

		Pageable paging = PageRequest.of(page, limit, _sort);

		Page<Enterprise> enterprises = enterpriseRepository.findAll(paging);

		pageContent.setContent(mapEntitysToDtos(enterprises.getContent()));
		pageContent.setTotalElements(enterprises.getTotalElements());

		return pageContent;
	}

	public PageContent<EnterpriseDto> findAllEnterprisesBySearch(String searchWord, Integer page, Integer limit,
			String sort, String dir) {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Enterprise> cq = cb.createQuery(Enterprise.class);
		Root<Enterprise> root = cq.from(Enterprise.class);

		PageContent<EnterpriseDto> pageContent = new PageContent<EnterpriseDto>();

		List<Predicate> predicates = new ArrayList<>();

		if (dir.equalsIgnoreCase(Sort.Direction.ASC.name()))
			cq.orderBy(cb.asc(root.get(sort)));
		else
			cq.orderBy(cb.desc(root.get(sort)));

		if (searchWord != null) {

			predicates.add(
					cb.like(cb.lower(root.get("address")), "%" + searchWord.toLowerCase().replace("'", "''") + "%"));

			if (StringUtils.isNumeric(searchWord))
				predicates.add(cb.equal(root.get("tva"), searchWord.replace("'", "''")));

		}

		Predicate finalPredicate = cb.or(predicates.toArray(new Predicate[] {}));

		TypedQuery<Enterprise> typedQuery = entityManager.createQuery(cq.select(root).where(finalPredicate));

		int total = typedQuery.getResultList().size();

		if (limit != -1)
			typedQuery.setFirstResult(page * limit).setMaxResults(limit);

		List<Enterprise> enterprises = typedQuery.getResultList();

		pageContent.setContent(mapEntitysToDtos(enterprises));
		pageContent.setTotalElements(total);

		return pageContent;
	}

	public EnterpriseDto findEnterpriseById(Long id) {
		Enterprise enterprise = enterpriseRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Enterprise not exist with id " + id));

		return mapEntityToDto(enterprise);
	}

	@Transactional
	public String deleteEnterprise(Long id) {
		Enterprise enterprise = enterpriseRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Impossible to delete entreprise with id " + id));

		for (Contact contact : enterprise.getContacts()) {
			contact.getEnterprises().remove(enterprise);
		}

		enterpriseRepository.delete(enterprise);

		return "Enterprise with id " + id + " has been deleted succussfully";
	}

	EnterpriseDto mapEntityToDto(Enterprise enterprise) {

		EnterpriseDto enterpriseDto = new EnterpriseDto();

		enterpriseDto.setId(enterprise.getId());
		enterpriseDto.setAddress(enterprise.getAddress());
		enterpriseDto.setTva(enterprise.getTva());

		return enterpriseDto;

	}

	Enterprise mapDtoToEntity(EnterpriseDto enterpriseDto) {

		Enterprise enterprise = new Enterprise();
		enterprise.setId(enterpriseDto.getId());
		enterprise.setAddress(enterpriseDto.getAddress());
		enterprise.setTva(enterpriseDto.getTva());

		return enterprise;

	}

	public List<EnterpriseDto> mapEntitysToDtos(List<Enterprise> enterprises) {
		return enterprises.stream().map(dto -> mapEntityToDto(dto)).filter(dto -> dto != null)
				.collect(Collectors.toList());
	}

}
