package com.poliscrypts.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

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

		Sort _sort = dir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sort).ascending()
				: Sort.by(sort).descending();

		Pageable paging = PageRequest.of(page, limit, _sort);

		Page<Enterprise> enterprises = enterpriseRepository.findAll(paging);

		PageContent<EnterpriseDto> pageContent = new PageContent<EnterpriseDto>();
		pageContent.setContent(mapEntitysToDtos(enterprises.getContent()));
		pageContent.setTotalElements(enterprises.getTotalElements());

		return pageContent;
	}

	public PageContent<EnterpriseDto> findAllEnterprisesByAddress(String address, Integer page, Integer limit,
			String sort, String dir) {

		Sort _sort = dir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sort).ascending()
				: Sort.by(sort).descending();

		Pageable paging = PageRequest.of(page, limit, _sort);

		Page<Enterprise> enterprises = enterpriseRepository.findByAddressContainingIgnoreCase(address, paging);

		PageContent<EnterpriseDto> pageContent = new PageContent<EnterpriseDto>();
		pageContent.setContent(mapEntitysToDtos(enterprises.getContent()));
		pageContent.setTotalElements(enterprises.getTotalElements());

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
