package com.poliscrypts.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.poliscrypts.dto.ContactDto;
import com.poliscrypts.exception.EntityNotFoundException;
import com.poliscrypts.exception.GlobalException;
import com.poliscrypts.model.Contact;
import com.poliscrypts.model.Enterprise;
import com.poliscrypts.repository.ContactRepository;
import com.poliscrypts.repository.EnterpriseRepository;
import com.poliscrypts.util.PageContent;

@Service
public class ContactService {

	@Autowired
	private ContactRepository contactRepository;

	@Autowired
	private EnterpriseRepository enterpriseRepository;

	@PersistenceContext
	private EntityManager entityManager;

	public ContactDto createContact(ContactDto contactDto) {
		Contact contact = mapDtoToEntity(contactDto);
		Set<Enterprise> enterprises = contact.getEnterprises();
		if (enterprises != null) {

		}

		return mapEntityToDto(contactRepository.save(contact));
	}

	public ContactDto updateContact(Long id, ContactDto contactDto) {

		contactRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Impossible to update this contact with id " + id));

		Contact contact = mapDtoToEntity(contactDto);

		contact.setId(id);

		Set<Enterprise> enterprises = contact.getEnterprises();
		if (enterprises != null) {
			enterprises.forEach(entreprise -> {
				enterpriseRepository.findById(entreprise.getId()).orElseThrow(() -> new GlobalException(
						"Impossible to update a contact with entreprise id " + entreprise.getId()));
			});
		}

		return mapEntityToDto(contactRepository.save(contact));
	}

	public PageContent<ContactDto> findBySearch(String searchWord, Integer page, Integer limit, String sort,
			String dir) {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Contact> cq = cb.createQuery(Contact.class);
		Root<Contact> root = cq.from(Contact.class);

		PageContent<ContactDto> pageContent = new PageContent<ContactDto>();

		Join<Contact, Enterprise> contactJoinEnterprise = root.join("enterprises", JoinType.LEFT);

		if (dir.equalsIgnoreCase(Sort.Direction.ASC.name()))
			cq.orderBy(cb.asc(root.get(sort)));
		else
			cq.orderBy(cb.desc(root.get(sort)));

		List<Predicate> predicates = new ArrayList<>();

		if (searchWord != null) {
			predicates.add(
					cb.like(cb.lower(root.get("firstName")), "%" + searchWord.toLowerCase().replace("'", "''") + "%"));

			predicates.add(
					cb.like(cb.lower(root.get("lastName")), "%" + searchWord.toLowerCase().replace("'", "''") + "%"));

			predicates.add(
					cb.like(cb.lower(root.get("address")), "%" + searchWord.toLowerCase().replace("'", "''") + "%"));

			predicates
					.add(cb.like(cb.lower(root.get("type")), "%" + searchWord.toLowerCase().replace("'", "''") + "%"));

			if (StringUtils.isNumeric(searchWord))
				predicates.add(cb.equal(root.get("tva"), searchWord.replace("'", "''")));

			predicates.add(cb.like(cb.lower(contactJoinEnterprise.get("address")),
					"%" + searchWord.toLowerCase().replace("'", "''") + "%"));

		}

		Predicate finalPredicate = cb.or(predicates.toArray(new Predicate[] {}));

		TypedQuery<Contact> typedQuery = entityManager.createQuery(cq.select(root).where(finalPredicate));

		int total = typedQuery.getResultList().size();

		if (limit != -1)
			typedQuery.setFirstResult(page * limit).setMaxResults(limit);

		List<Contact> contacts = typedQuery.getResultList();

		pageContent.setContent(mapEntitysToDtos(contacts));
		pageContent.setTotalElements(total);

		return pageContent;

	}

	public PageContent<ContactDto> getAllContacs(Integer page, Integer limit, String sort, String dir) {

		PageContent<ContactDto> pageContent = new PageContent<ContactDto>();

		if (limit == -1)
			limit = enterpriseRepository.findAll().size();

		Sort _sort = dir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sort).ascending()
				: Sort.by(sort).descending();

		Pageable paging = PageRequest.of(page, limit, _sort);

		Page<Contact> contacts = contactRepository.findAll(paging);

		pageContent.setContent(mapEntitysToDtos(contacts.getContent()));
		pageContent.setTotalElements(contacts.getTotalElements());

		return pageContent;
	}

	public ContactDto findContactById(Long id) {
		Contact contact = contactRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("A Contact not exist with id " + id));

		return mapEntityToDto(contact);
	}

	public String deleteContact(Long id) {
		Contact contact = contactRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Impossible de delete a contact with id " + id));

		contactRepository.delete(contact);

		return "Contact with id " + id + " has been deleted succussfully";

	}

	ContactDto mapEntityToDto(Contact contact) {

		ContactDto contactDto = new ContactDto();
		contactDto.setId(contact.getId());
		contactDto.setFirstName(contact.getFirstName());
		contactDto.setLastName(contact.getLastName());
		contactDto.setType(contact.getType());
		contactDto.setAddress(contact.getAddress());
		contactDto.setTva(contact.getTva());
		contactDto
				.setEnterprises(contact.getEnterprises().stream().map(ent -> ent.getId()).collect(Collectors.toSet()));

		return contactDto;

	}

	Contact mapDtoToEntity(ContactDto contactDto) {

		Contact contact = new Contact();
		contact.setId(contactDto.getId());
		contact.setFirstName(contactDto.getFirstName());
		contact.setLastName(contactDto.getLastName());
		contact.setType(contactDto.getType());
		contact.setAddress(contactDto.getAddress());
		contact.setTva(contactDto.getTva());
		Set<Enterprise> enterprises = new HashSet<>();

		for (Long id : contactDto.getEnterprises()) {
			Enterprise enterprise = enterpriseRepository.findById(id)
					.orElseThrow(() -> new GlobalException("Impossible to create contact with entreprise id " + id));

			enterprises.add(enterprise);
		}

		contact.setEnterprises(enterprises);

		return contact;

	}

	public List<ContactDto> mapEntitysToDtos(List<Contact> contacts) {
		return contacts.stream().map(dto -> mapEntityToDto(dto)).filter(dto -> dto != null)
				.collect(Collectors.toList());
	}

}
