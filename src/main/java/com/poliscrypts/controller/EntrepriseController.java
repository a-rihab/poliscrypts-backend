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

@RestController
@RequestMapping("/api/entreprise")
public class EntrepriseController {

	@Autowired
	private EntrepriseService entrepriseService;

	@PostMapping
	public ResponseEntity<Entreprise> saveEntreprise(@RequestBody Entreprise entreprise) {

		Entreprise savedEntreprise = entrepriseService.createEntreprise(entreprise);

		return new ResponseEntity<Entreprise>(savedEntreprise, HttpStatus.CREATED);

	}

	@GetMapping
	public ResponseEntity<PageContent<Entreprise>> getAllEntreprises(@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "10") Integer limit, @RequestParam(defaultValue = "createDate") String sort,
			@RequestParam(defaultValue = "desc") String dir) {

		PageContent<Entreprise> pageDto = entrepriseService.getAllContacs(page, limit, sort, dir);
		return new ResponseEntity<PageContent<Entreprise>>(pageDto, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Entreprise> getAllEntreprises(@PathVariable Long id) {

		Entreprise entreprise = entrepriseService.findEntrepriseById(id);
		return new ResponseEntity<Entreprise>(entreprise, HttpStatus.FOUND);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Entreprise> updateEntreprise(@PathVariable Long id, @RequestBody Entreprise entreprise) {

		Entreprise updatedEntreprise = entrepriseService.updateEntreprise(id, entreprise);

		return new ResponseEntity<Entreprise>(updatedEntreprise, HttpStatus.OK);

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteEntreprise(@PathVariable Long id) {

		String response = entrepriseService.deleteEntreprise(id);

		return new ResponseEntity<String>(response, HttpStatus.OK);

	}

}
