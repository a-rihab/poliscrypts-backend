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

import com.poliscrypts.dto.EnterpriseDto;
import com.poliscrypts.exception.ValidationException;
import com.poliscrypts.model.Enterprise;
import com.poliscrypts.service.EnterpriseService;
import com.poliscrypts.util.PageContent;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/enterprise")
public class EntrepriseController {

	@Autowired
	private EnterpriseService enterpriseService;

	@Operation(summary = "Create a enterprise")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Entreprise has been created successfully !", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Enterprise.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping
	public ResponseEntity<?> saveEntreprise(
			@Parameter(description = "Provide a enterprise payload", required = true) @Valid @RequestBody EnterpriseDto enterpriseDto,
			BindingResult results) {

		Map<String, String> errors = new HashMap<>();
		EnterpriseDto savedEntreprise = null;

		try {
			if (results.hasErrors()) {

				results.getAllErrors().forEach((error) -> {
					String fieldName = ((FieldError) error).getField();
					String errorMessage = error.getDefaultMessage();
					errors.put(fieldName, errorMessage);
				});

				throw new ValidationException(errors);

			}

			savedEntreprise = enterpriseService.createEntreprise(enterpriseDto);

		} catch (Exception e) {
			return new ResponseEntity<Map<String, String>>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<EnterpriseDto>(savedEntreprise, HttpStatus.CREATED);

	}

	@Operation(summary = "Get all enterprises ")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Retrieve all enterprises", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Enterprise.class))) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error") })

	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	@GetMapping
	public ResponseEntity<PageContent<EnterpriseDto>> getAllEntreprises(
			@Parameter(description = "Provide a page number") @RequestParam(defaultValue = "0") Integer page,
			@Parameter(description = "Provide a limit number") @RequestParam(defaultValue = "10") Integer limit,
			@Parameter(description = "Provide a sort field") @RequestParam(defaultValue = "createDate") String sort,
			@Parameter(description = "Provide a direction") @RequestParam(defaultValue = "desc") String dir) {

		PageContent<EnterpriseDto> pageDto = enterpriseService.getAllEnterprises(page, limit, sort, dir);
		return new ResponseEntity<PageContent<EnterpriseDto>>(pageDto, HttpStatus.OK);
	}

	@Operation(summary = "Get all enterprises by address ")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Retrieve all enterprises", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Enterprise.class))) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error") })

	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	@GetMapping("/search")
	public ResponseEntity<PageContent<EnterpriseDto>> getAllEntreprisesByAddress(
			@Parameter(description = "Provide a address") @RequestParam String searchWord,
			@Parameter(description = "Provide a page number") @RequestParam(defaultValue = "0") Integer page,
			@Parameter(description = "Provide a limit number") @RequestParam(defaultValue = "10") Integer limit,
			@Parameter(description = "Provide a sort field") @RequestParam(defaultValue = "createDate") String sort,
			@Parameter(description = "Provide a direction") @RequestParam(defaultValue = "desc") String dir) {

		PageContent<EnterpriseDto> pageDto = enterpriseService.findAllEnterprisesByAddress(searchWord, page, limit,
				sort, dir);
		return new ResponseEntity<PageContent<EnterpriseDto>>(pageDto, HttpStatus.OK);
	}

	@Operation(summary = "Get a enterprise by its id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the enterprise", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Enterprise.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "Entreprise not found", content = @Content) })

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/{enterpriseId}")
	public ResponseEntity<EnterpriseDto> getEntrepriseById(
			@Parameter(description = "Provide a enterprise id", required = true) @PathVariable Long enterpriseId) {

		EnterpriseDto enterpriseDto = enterpriseService.findEntrepriseById(enterpriseId);
		return new ResponseEntity<EnterpriseDto>(enterpriseDto, HttpStatus.FOUND);
	}

	@Operation(summary = "Update a enterprise by its id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Update the enterprise", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Enterprise.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "Entreprise not found", content = @Content) })

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/{enterpriseId}")
	public ResponseEntity<?> updateEntreprise(
			@Parameter(description = "Provide a enterprise id", required = true) @PathVariable Long enterpriseId,
			@Parameter(description = "Provide a enterprise id", required = true) @Valid @RequestBody EnterpriseDto enterpriseDto,
			BindingResult results) {

		Map<String, String> errors = new HashMap<>();
		EnterpriseDto updatedEntreprise = null;

		try {
			if (results.hasErrors()) {

				results.getAllErrors().forEach((error) -> {
					String fieldName = ((FieldError) error).getField();
					String errorMessage = error.getDefaultMessage();
					errors.put(fieldName, errorMessage);
				});

				throw new ValidationException(errors);

			}

			updatedEntreprise = enterpriseService.updateEntreprise(enterpriseId, enterpriseDto);

		} catch (Exception e) {
			return new ResponseEntity<Map<String, String>>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<EnterpriseDto>(updatedEntreprise, HttpStatus.OK);

	}

	@Operation(summary = "Delete a enterprise by its id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Delete the enterprise", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Enterprise.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "Entreprise not found", content = @Content) })

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/{enterpriseId}")
	public ResponseEntity<String> deleteEntreprise(
			@Parameter(description = "Provide a enterprise id", required = true) @PathVariable Long enterpriseId) {

		String response = enterpriseService.deleteEntreprise(enterpriseId);

		return new ResponseEntity<String>(response, HttpStatus.OK);

	}

}
