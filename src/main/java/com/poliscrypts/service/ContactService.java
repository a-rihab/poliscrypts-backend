package com.poliscrypts.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.poliscrypts.model.Entreprise;
import com.poliscrypts.repository.ContactRepository;
import com.poliscrypts.repository.EntrepriseRepository;
import com.poliscrypts.util.PageContent;

@Service
public class ContactService {

	@Autowired
	private ContactRepository contactRepository;

	@Autowired
	private EntrepriseRepository entrepriseRepository;

	public ContactDto createContact(ContactDto contactDto) {
		Contact contact = mapDtoToEntity(contactDto);
		Set<Entreprise> entreprises = contact.getEntreprises();
		if (entreprises != null) {

			entreprises.forEach(entreprise -> {
				entrepriseRepository.findById(entreprise.getId()).orElseThrow(() -> new GlobalException(
						"Impossible de créer un contact avec une entreprise dont l'id :" + entreprise.getId()));
			});

		}

		return mapEntityToDto(contactRepository.save(contact));
	}

	public ContactDto updateContact(Long id, ContactDto contactDto) {

		Contact oldContact = contactRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Impossible de modifier ce contact"));

		Contact contact = mapDtoToEntity(contactDto);

		contact.setId(oldContact.getId());

		Set<Entreprise> entreprises = contact.getEntreprises();
		if (entreprises != null) {
			entreprises.forEach(entreprise -> {
				entrepriseRepository.findById(entreprise.getId()).orElseThrow(() -> new GlobalException(
						"Impossible de mettre à jour ce contact avec une entreprise dont l'id :" + entreprise.getId()));
			});
		}

		return mapEntityToDto(contactRepository.save(contact));
	}

	public PageContent<ContactDto> getAllContacs(Integer page, Integer limit, String sort, String dir) {

		Pageable paging = null;

		if (dir.equals("asc"))
			paging = PageRequest.of(page, limit, Sort.by(sort).ascending());
		else
			paging = PageRequest.of(page, limit, Sort.by(sort).descending());

		Page<Contact> contacts = contactRepository.findAll(paging);

		PageContent<ContactDto> pageContent = new PageContent<ContactDto>();
		pageContent.setContent(mapEntitysToDtos(contacts.getContent()));
		pageContent.setTotalElements(contacts.getTotalElements());

		return pageContent;
	}

	public PageContent<ContactDto> findAllEntreprisesBySearch(String search, Integer page, Integer limit, String sort,
			String dir) {

		Pageable paging = null;

		if (dir.equals("asc"))
			paging = PageRequest.of(page, limit, Sort.by(sort).ascending());
		else
			paging = PageRequest.of(page, limit, Sort.by(sort).descending());

		Page<Contact> contacts = contactRepository
				.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrAddressContainingIgnoreCase(search,
						search, search, paging);

		PageContent<ContactDto> pageContent = new PageContent<ContactDto>();
		pageContent.setContent(mapEntitysToDtos(contacts.getContent()));
		pageContent.setTotalElements(contacts.getTotalElements());

		return pageContent;
	}

	public ContactDto findContactById(Long id) {
		Contact contact = contactRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Ce contact n'existe pas"));

		return mapEntityToDto(contact);
	}

	public String deleteContact(Long id) {
		Contact contact = contactRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Impossible de supprimer ce contact"));

		contactRepository.delete(contact);

		return "Contact with id " + id + " has been deleting succussfully";

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
				.setEntreprises(contact.getEntreprises().stream().map(ent -> ent.getId()).collect(Collectors.toList()));

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
		Set<Entreprise> entreprises = new HashSet<>();

		for (Long id : contactDto.getEntreprises()) {
			Entreprise entreprise = entrepriseRepository.findById(id).orElse(null);
			if (entreprise != null)
				entreprises.add(entreprise);
		}

		contact.setEntreprises(entreprises);

		return contact;

	}

	public List<ContactDto> mapEntitysToDtos(List<Contact> contacts) {
		return contacts.stream().map(dto -> mapEntityToDto(dto)).filter(dto -> dto != null)
				.collect(Collectors.toList());
	}

}
