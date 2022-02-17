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
	@PostMapping
	public ResponseEntity<Entreprise> saveEntreprise(
			@Parameter(description = "Provide a entreprise payload", required = true) @RequestBody Entreprise entreprise) {

		Entreprise savedEntreprise = entrepriseService.createEntreprise(entreprise);

		return new ResponseEntity<Entreprise>(savedEntreprise, HttpStatus.CREATED);

	}

	@Operation(summary = "Get all entreprises ")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Retrieve all resources", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Entreprise.class))) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error") })
	@GetMapping
	public ResponseEntity<PageContent<Entreprise>> getAllEntreprises(
			@Parameter(description = "Provide a page number") @RequestParam(defaultValue = "0") Integer page,
			@Parameter(description = "Provide a limit number") @RequestParam(defaultValue = "10") Integer limit,
			@Parameter(description = "Provide a sort field") @RequestParam(defaultValue = "createDate") String sort,
			@Parameter(description = "Provide a direction") @RequestParam(defaultValue = "desc") String dir) {

		PageContent<Entreprise> pageDto = entrepriseService.getAllContacs(page, limit, sort, dir);
		return new ResponseEntity<PageContent<Entreprise>>(pageDto, HttpStatus.OK);
	}

	@Operation(summary = "Get a entreprise by its id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the entreprise", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Entreprise.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "Entreprise not found", content = @Content) })
	@GetMapping("/{id}")
	public ResponseEntity<Entreprise> getEntrepriseById(
			@Parameter(description = "Provide a entreprise id", required = true) @PathVariable Long id) {

		Entreprise entreprise = entrepriseService.findEntrepriseById(id);
		return new ResponseEntity<Entreprise>(entreprise, HttpStatus.FOUND);
	}

	@Operation(summary = "Update a entreprise by its id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Update the entreprise", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Entreprise.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "Entreprise not found", content = @Content) })
	@PutMapping("/{id}")
	public ResponseEntity<Entreprise> updateEntreprise(
			@Parameter(description = "Provide a entreprise id", required = true) @PathVariable Long id,
			@Parameter(description = "Provide a entreprise id", required = true) @RequestBody Entreprise entreprise) {

		Entreprise updatedEntreprise = entrepriseService.updateEntreprise(id, entreprise);

		return new ResponseEntity<Entreprise>(updatedEntreprise, HttpStatus.OK);

	}

	@Operation(summary = "Delete a entreprise by its id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Delete the entreprise", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Entreprise.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "Entreprise not found", content = @Content) })
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteEntreprise(
			@Parameter(description = "Provide a entreprise id", required = true) @PathVariable Long id) {

		String response = entrepriseService.deleteEntreprise(id);

		return new ResponseEntity<String>(response, HttpStatus.OK);

	}

}
