package com.poliscrypts.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
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
public class EnterpriseController {

	@Autowired
	private EnterpriseService enterpriseService;

	@Operation(summary = "Create a enterprise")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Enterprise has been created successfully !", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Enterprise.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping
	public ResponseEntity<?> saveEnterprise(
			@Parameter(description = "Provide a enterprise payload", required = true) @Valid @RequestBody EnterpriseDto enterpriseDto) {

		EnterpriseDto savedEnterprise = enterpriseService.createEnterprise(enterpriseDto);

		return new ResponseEntity<EnterpriseDto>(savedEnterprise, HttpStatus.CREATED);

	}

	@Operation(summary = "Get all enterprises ")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Retrieve all enterprises", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Enterprise.class))) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error") })

	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	@GetMapping
	public ResponseEntity<PageContent<EnterpriseDto>> getAllEnterprises(
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
	public ResponseEntity<PageContent<EnterpriseDto>> getAllEnterprisesByAddress(
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
			@ApiResponse(responseCode = "404", description = "Enterprise not found", content = @Content) })

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/{enterpriseId}")
	public ResponseEntity<EnterpriseDto> getEnterpriseById(
			@Parameter(description = "Provide a enterprise id", required = true) @PathVariable Long enterpriseId) {

		EnterpriseDto enterpriseDto = enterpriseService.findEnterpriseById(enterpriseId);
		return new ResponseEntity<EnterpriseDto>(enterpriseDto, HttpStatus.FOUND);
	}

	@Operation(summary = "Update a enterprise by its id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Update the enterprise", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Enterprise.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "Enterprise not found", content = @Content) })

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/{enterpriseId}")
	public ResponseEntity<?> updateEnterprise(
			@Parameter(description = "Provide a enterprise id", required = true) @PathVariable Long enterpriseId,
			@Parameter(description = "Provide a enterprise id", required = true) @Valid @RequestBody EnterpriseDto enterpriseDto,
			BindingResult results) {

		EnterpriseDto updatedEnterprise = enterpriseService.updateEnterprise(enterpriseId, enterpriseDto);

		return new ResponseEntity<EnterpriseDto>(updatedEnterprise, HttpStatus.OK);

	}

	@Operation(summary = "Delete a enterprise by its id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Delete the enterprise", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Enterprise.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "Enterprise not found", content = @Content) })

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/{enterpriseId}")
	public ResponseEntity<String> deleteEnterprise(
			@Parameter(description = "Provide a enterprise id", required = true) @PathVariable Long enterpriseId) {

		String response = enterpriseService.deleteEnterprise(enterpriseId);

		return new ResponseEntity<String>(response, HttpStatus.OK);

	}

}
