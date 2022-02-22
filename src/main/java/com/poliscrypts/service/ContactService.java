package com.poliscrypts.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

	public Contact createContact(Contact contact) {
		List<Entreprise> entreprises = contact.getEntreprises();
		if (entreprises != null) {

			entreprises.forEach(entreprise -> {
				entrepriseRepository.findById(entreprise.getId()).orElseThrow(() -> new GlobalException(
						"Impossible de créer un contact avec une entreprise dont l'id :" + entreprise.getId()));
			});

		}

		return contactRepository.save(contact);
	}

	public Contact updateContact(Long id, Contact contact) {

		Contact oldContact = contactRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Impossible de modifier ce contact"));
		contact.setId(oldContact.getId());

		List<Entreprise> entreprises = contact.getEntreprises();
		if (entreprises != null) {
			entreprises.forEach(entreprise -> {
				entrepriseRepository.findById(entreprise.getId()).orElseThrow(() -> new GlobalException(
						"Impossible de mettre à jour ce contact avec une entreprise dont l'id :" + entreprise.getId()));
			});
		}

		return contactRepository.save(contact);
	}

	public PageContent<Contact> getAllContacs(Integer page, Integer limit, String sort, String dir) {

		Pageable paging = null;

		if (dir.equals("asc"))
			paging = PageRequest.of(page, limit, Sort.by(sort).ascending());
		else
			paging = PageRequest.of(page, limit, Sort.by(sort).descending());

		Page<Contact> contacts = contactRepository.findAll(paging);

		PageContent<Contact> pageContent = new PageContent<Contact>();
		pageContent.setContent(contacts.getContent());
		pageContent.setTotalElements(contacts.getTotalElements());

		return pageContent;
	}

	public PageContent<Contact> findAllEntreprisesBySearch(String search, Integer page, Integer limit, String sort,
			String dir) {

		Pageable paging = null;

		if (dir.equals("asc"))
			paging = PageRequest.of(page, limit, Sort.by(sort).ascending());
		else
			paging = PageRequest.of(page, limit, Sort.by(sort).descending());

		Page<Contact> contacts = contactRepository
				.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrAddressContainingIgnoreCase(search,
						search, search, paging);

		PageContent<Contact> pageContent = new PageContent<Contact>();
		pageContent.setContent(contacts.getContent());
		pageContent.setTotalElements(contacts.getTotalElements());

		return pageContent;
	}

	public Contact findContactById(Long id) {
		return contactRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Ce contact n'existe pas"));
	}

	public String deleteContact(Long id) {
		Contact contact = contactRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Impossible de supprimer ce contact"));

		contactRepository.delete(contact);

		return "Contact with id " + id + " has been deleting succussfully";

	}

}
