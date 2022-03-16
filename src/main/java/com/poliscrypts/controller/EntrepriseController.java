package com.poliscrypts.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poliscrypts.dto.EntrepriseDto;
import com.poliscrypts.exception.ValidationException;
import com.poliscrypts.model.Entreprise;
import com.poliscrypts.service.EntrepriseService;
import com.poliscrypts.util.PageContent;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/entreprise")
public class EntrepriseController {

	@Autowired
	private EntrepriseService entrepriseService;

	@Operation(summary = "Create a entreprise")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Entreprise has been created successfully !", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Entreprise.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })

	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping
	public ResponseEntity<?> saveEntreprise(
			@Parameter(description = "Provide a entreprise payload", required = true) @Valid @RequestBody EntrepriseDto entrepriseDto,
			BindingResult results) {

		Map<String, String> errors = new HashMap<>();
		EntrepriseDto savedEntreprise = null;

		try {
			if (results.hasErrors()) {

				results.getAllErrors().forEach((error) -> {
					String fieldName = ((FieldError) error).getField();
					String errorMessage = error.getDefaultMessage();
					errors.put(fieldName, errorMessage);
				});

				throw new ValidationException(errors);

			}

			savedEntreprise = entrepriseService.createEntreprise(entrepriseDto);

		} catch (Exception e) {
			return new ResponseEntity<Map<String, String>>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<EntrepriseDto>(savedEntreprise, HttpStatus.CREATED);

	}

	@Operation(summary = "Get all entreprises ")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Retrieve all entreprises", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Entreprise.class))) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error") })

	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	@GetMapping
	public ResponseEntity<PageContent<EntrepriseDto>> getAllEntreprises(
			@Parameter(description = "Provide a page number") @RequestParam(defaultValue = "0") Integer page,
			@Parameter(description = "Provide a limit number") @RequestParam(defaultValue = "10") Integer limit,
			@Parameter(description = "Provide a sort field") @RequestParam(defaultValue = "createDate") String sort,
			@Parameter(description = "Provide a direction") @RequestParam(defaultValue = "desc") String dir) {

		PageContent<EntrepriseDto> pageDto = entrepriseService.getAllEntreprises(page, limit, sort, dir);
		return new ResponseEntity<PageContent<EntrepriseDto>>(pageDto, HttpStatus.OK);
	}

	@Operation(summary = "Get all entreprises by address ")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Retrieve all entreprises", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Entreprise.class))) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error") })

	// @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	@GetMapping("/search")
	public ResponseEntity<PageContent<EntrepriseDto>> getAllEntreprisesByAddress(
			@Parameter(description = "Provide a address") @RequestParam String searchWord,
			@Parameter(description = "Provide a page number") @RequestParam(defaultValue = "0") Integer page,
			@Parameter(description = "Provide a limit number") @RequestParam(defaultValue = "10") Integer limit,
			@Parameter(description = "Provide a sort field") @RequestParam(defaultValue = "createDate") String sort,
			@Parameter(description = "Provide a direction") @RequestParam(defaultValue = "desc") String dir) {

		PageContent<EntrepriseDto> pageDto = entrepriseService.findAllEntreprisesByAddress(searchWord, page, limit,
				sort, dir);
		return new ResponseEntity<PageContent<EntrepriseDto>>(pageDto, HttpStatus.OK);
	}

	@Operation(summary = "Get a entreprise by its id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the entreprise", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Entreprise.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "Entreprise not found", content = @Content) })

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity<EntrepriseDto> getEntrepriseById(
			@Parameter(description = "Provide a entreprise id", required = true) @PathVariable Long id) {

		EntrepriseDto entrepriseDto = entrepriseService.findEntrepriseById(id);
		return new ResponseEntity<EntrepriseDto>(entrepriseDto, HttpStatus.FOUND);
	}

	@Operation(summary = "Update a entreprise by its id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Update the entreprise", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Entreprise.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "Entreprise not found", content = @Content) })

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<?> updateEntreprise(
			@Parameter(description = "Provide a entreprise id", required = true) @PathVariable Long id,
			@Parameter(description = "Provide a entreprise id", required = true) @Valid @RequestBody EntrepriseDto entrepriseDto,
			BindingResult results) {

		Map<String, String> errors = new HashMap<>();
		EntrepriseDto updatedEntreprise = null;

		try {
			if (results.hasErrors()) {

				results.getAllErrors().forEach((error) -> {
					String fieldName = ((FieldError) error).getField();
					String errorMessage = error.getDefaultMessage();
					errors.put(fieldName, errorMessage);
				});

				throw new ValidationException(errors);

			}

			updatedEntreprise = entrepriseService.updateEntreprise(id, entrepriseDto);

		} catch (Exception e) {
			return new ResponseEntity<Map<String, String>>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<EntrepriseDto>(updatedEntreprise, HttpStatus.OK);

	}

	@Operation(summary = "Delete a entreprise by its id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Delete the entreprise", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Entreprise.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "Entreprise not found", content = @Content) })

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteEntreprise(
			@Parameter(description = "Provide a entreprise id", required = true) @PathVariable Long id) {

		String response = entrepriseService.deleteEntreprise(id);

		return new ResponseEntity<String>(response, HttpStatus.OK);

	}

}
