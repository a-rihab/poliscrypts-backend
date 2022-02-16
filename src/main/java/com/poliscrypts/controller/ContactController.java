package com.poliscrypts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poliscrypts.model.Contact;
import com.poliscrypts.service.ContactService;
import com.poliscrypts.util.PageContent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/contact")
public class ContactController {

	@Autowired
	private ContactService contactService;

	@PostMapping
	public ResponseEntity<Contact> saveContact(@RequestBody Contact contact) {

		Contact savedContact = contactService.createContact(contact);

		return new ResponseEntity<Contact>(savedContact, HttpStatus.CREATED);

	}

	@GetMapping
	public ResponseEntity<PageContent<Contact>> getAllContacts(@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "10") Integer limit, @RequestParam(defaultValue = "createDate") String sort,
			@RequestParam(defaultValue = "desc") String dir) {

		PageContent<Contact> pageDto = contactService.getAllContacs(page, limit, sort, dir);
		return new ResponseEntity<PageContent<Contact>>(pageDto, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Contact> getAllContacts(@PathVariable Long id) {

		Contact contact = contactService.findContactById(id);
		return new ResponseEntity<Contact>(contact, HttpStatus.FOUND);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Contact> updateContact(@PathVariable Long id, @RequestBody Contact contact) {

		Contact updatedContact = contactService.updateContact(id, contact);

		return new ResponseEntity<Contact>(updatedContact, HttpStatus.OK);

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteContact(@PathVariable Long id) {

		String response = contactService.deleteContact(id);

		return new ResponseEntity<String>(response, HttpStatus.OK);

	}

}
