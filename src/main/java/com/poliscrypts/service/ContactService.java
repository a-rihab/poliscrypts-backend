package com.poliscrypts.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.poliscrypts.exception.EntityNotFoundException;
import com.poliscrypts.model.Contact;
import com.poliscrypts.repository.ContactRepository;
import com.poliscrypts.util.PageContent;

@Service
public class ContactService {

	@Autowired
	private ContactRepository contactRepository;

	public Contact createContact(Contact contact) {
		return contactRepository.save(contact);
	}

	public Contact updateContact(Long id, Contact contact) {

		Optional<Contact> optional = contactRepository.findById(id);
		Contact oldContact = null;

		if (optional.isPresent()) {
			oldContact = optional.get();
			contact.setId(oldContact.getId());
		} else {
			throw new EntityNotFoundException("Impossible de modifier ce contact");
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

	public Contact findContactById(Long id) {

		Contact contact = contactRepository.findById(id).orElse(null);
		if (contact == null) {
			throw new EntityNotFoundException("Ce contact n'existe pas");
		}
		return contact;
	}

	public String deleteContact(Long id) {
		Contact contact = contactRepository.findById(id).orElse(null);
		String message = "";

		if (contact != null) {
			contactRepository.delete(contact);
			message = "Contact with id " + id + " has been deleting succussfully";
		} else {
			throw new EntityNotFoundException("Impossible de supprimer ce contact");
		}
		return message;
	}

}
