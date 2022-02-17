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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

	@Autowired
	private ContactService contactService;

	@Operation(summary = "Create a contact")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Contact has been created successfully !", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Contact.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
	@PostMapping
	public ResponseEntity<Contact> saveContact(
			@Parameter(description = "Provide a contact payload", required = true) @RequestBody Contact contact) {

		Contact savedContact = contactService.createContact(contact);

		return new ResponseEntity<Contact>(savedContact, HttpStatus.CREATED);

	}

	@Operation(summary = "Get all contacts ")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Retrieve all resources", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Contact.class))) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error") })
	@GetMapping
	public ResponseEntity<PageContent<Contact>> getAllContacts(
			@Parameter(description = "Provide a page number") @RequestParam(defaultValue = "0") Integer page,
			@Parameter(description = "Provide a limit number") @RequestParam(defaultValue = "10") Integer limit,
			@Parameter(description = "Provide a sort field") @RequestParam(defaultValue = "createDate") String sort,
			@Parameter(description = "Provide a direction") @RequestParam(defaultValue = "desc") String dir) {

		PageContent<Contact> pageDto = contactService.getAllContacs(page, limit, sort, dir);
		return new ResponseEntity<PageContent<Contact>>(pageDto, HttpStatus.OK);
	}

	@Operation(summary = "Get a contact by its id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the contact", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Contact.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "Contact not found", content = @Content) })

	@GetMapping("/{id}")
	public ResponseEntity<Contact> getContactById(
			@Parameter(description = "Provide a contact id", required = true) @PathVariable Long id) {

		Contact contact = contactService.findContactById(id);
		return new ResponseEntity<Contact>(contact, HttpStatus.FOUND);
	}

	@Operation(summary = "Update a contact by its id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Update the contact", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Contact.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "Contact not found", content = @Content) })

	@PutMapping("/{id}")
	public ResponseEntity<Contact> updateContact(
			@Parameter(description = "Provide a payload of contact", required = true) @PathVariable Long id,
			@Parameter(description = "Provide a contact id", required = true) @RequestBody Contact contact) {

		Contact updatedContact = contactService.updateContact(id, contact);

		return new ResponseEntity<Contact>(updatedContact, HttpStatus.OK);

	}

	@Operation(summary = "Delete a contact by its id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Delete the contact", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Contact.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "Contact not found", content = @Content) })

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteContact(
			@Parameter(description = "Provide a contact id", required = true) @PathVariable Long id) {

		String response = contactService.deleteContact(id);

		return new ResponseEntity<String>(response, HttpStatus.OK);

	}

}
