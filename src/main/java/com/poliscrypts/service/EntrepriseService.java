package com.poliscrypts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.poliscrypts.exception.EntityNotFoundException;
import com.poliscrypts.model.Entreprise;
import com.poliscrypts.repository.EntrepriseRepository;
import com.poliscrypts.util.PageContent;

@Service
public class EntrepriseService {

	@Autowired
	private EntrepriseRepository entrepriseRepository;

	public Entreprise createEntreprise(Entreprise entreprise) {
		return entrepriseRepository.save(entreprise);
	}

	public Entreprise updateEntreprise(Long id, Entreprise entreprise) {

		Entreprise oldEntreprise = entrepriseRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Impossible de modifier ce entreprise"));
		entreprise.setId(oldEntreprise.getId());

		return entrepriseRepository.save(entreprise);
	}

	public PageContent<Entreprise> getAllEntreprises(Integer page, Integer limit, String sort, String dir) {

		Pageable paging = null;

		if (dir.equals("asc"))
			paging = PageRequest.of(page, limit, Sort.by(sort).ascending());
		else
			paging = PageRequest.of(page, limit, Sort.by(sort).descending());

		Page<Entreprise> entreprises = entrepriseRepository.findAll(paging);

		PageContent<Entreprise> pageContent = new PageContent<Entreprise>();
		pageContent.setContent(entreprises.getContent());
		pageContent.setTotalElements(entreprises.getTotalElements());

		return pageContent;
	}

	public PageContent<Entreprise> findAllEntreprisesByAddress(String address, Integer page, Integer limit, String sort,
			String dir) {

		Pageable paging = null;

		if (dir.equals("asc"))
			paging = PageRequest.of(page, limit, Sort.by(sort).ascending());
		else
			paging = PageRequest.of(page, limit, Sort.by(sort).descending());

		Page<Entreprise> entreprises = entrepriseRepository.findByAddressContainingIgnoreCase(address, paging);

		PageContent<Entreprise> pageContent = new PageContent<Entreprise>();
		pageContent.setContent(entreprises.getContent());
		pageContent.setTotalElements(entreprises.getTotalElements());

		return pageContent;
	}

	public Entreprise findEntrepriseById(Long id) {
		return entrepriseRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Cette entreprise n'existe pas"));
	}

	public String deleteEntreprise(Long id) {
		Entreprise entreprise = entrepriseRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Impossible de supprimer cette entreprise"));
		entrepriseRepository.delete(entreprise);

		return "Entreprise with id " + id + " has been deleting succussfully";
	}

}
