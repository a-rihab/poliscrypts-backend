package com.poliscrypts.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.poliscrypts.dto.EntrepriseDto;
import com.poliscrypts.exception.EntityNotFoundException;
import com.poliscrypts.model.Contact;
import com.poliscrypts.model.Entreprise;
import com.poliscrypts.repository.EntrepriseRepository;
import com.poliscrypts.util.PageContent;

@Service
public class EntrepriseService {

	@Autowired
	private EntrepriseRepository entrepriseRepository;

	public EntrepriseDto createEntreprise(EntrepriseDto entrepriseDto) {

		Entreprise entreprise = mapDtoToEntity(entrepriseDto);

		return mapEntityToDto(entrepriseRepository.save(entreprise));
	}

	public EntrepriseDto updateEntreprise(Long id, EntrepriseDto entrepriseDto) {

		Entreprise oldEntreprise = entrepriseRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Impossible to update entreprise with id " + id));

		Entreprise entreprise = mapDtoToEntity(entrepriseDto);

		entreprise.setId(oldEntreprise.getId());

		return mapEntityToDto(entrepriseRepository.save(entreprise));
	}

	public PageContent<EntrepriseDto> getAllEntreprises(Integer page, Integer limit, String sort, String dir) {

		Pageable paging = null;

		if (dir.equals("asc"))
			paging = PageRequest.of(page, limit, Sort.by(sort).ascending());
		else
			paging = PageRequest.of(page, limit, Sort.by(sort).descending());

		Page<Entreprise> entreprises = entrepriseRepository.findAll(paging);

		PageContent<EntrepriseDto> pageContent = new PageContent<EntrepriseDto>();
		pageContent.setContent(mapEntitysToDtos(entreprises.getContent()));
		pageContent.setTotalElements(entreprises.getTotalElements());

		return pageContent;
	}

	public PageContent<EntrepriseDto> findAllEntreprisesByAddress(String address, Integer page, Integer limit,
			String sort, String dir) {

		Pageable paging = null;

		if (dir.equals("asc"))
			paging = PageRequest.of(page, limit, Sort.by(sort).ascending());
		else
			paging = PageRequest.of(page, limit, Sort.by(sort).descending());

		Page<Entreprise> entreprises = entrepriseRepository.findByAddressContainingIgnoreCase(address, paging);

		PageContent<EntrepriseDto> pageContent = new PageContent<EntrepriseDto>();
		pageContent.setContent(mapEntitysToDtos(entreprises.getContent()));
		pageContent.setTotalElements(entreprises.getTotalElements());

		return pageContent;
	}

	public EntrepriseDto findEntrepriseById(Long id) {
		Entreprise entreprise = entrepriseRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Entreprise not exist with id " + id));

		return mapEntityToDto(entreprise);
	}

	@Transactional
	public String deleteEntreprise(Long id) {
		Entreprise entreprise = entrepriseRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Impossible to delete entreprise with id " + id));

		for (Contact contact : entreprise.getContacts()) {
			contact.getEntreprises().remove(entreprise);
		}

		entrepriseRepository.delete(entreprise);

		return "Entreprise with id " + id + " has been deleting succussfully";
	}

	EntrepriseDto mapEntityToDto(Entreprise entreprise) {

		EntrepriseDto entrepriseDto = new EntrepriseDto();

		entrepriseDto.setId(entreprise.getId());
		entrepriseDto.setAddress(entreprise.getAddress());
		entrepriseDto.setTva(entreprise.getTva());

		return entrepriseDto;

	}

	Entreprise mapDtoToEntity(EntrepriseDto entrepriseDto) {

		Entreprise entreprise = new Entreprise();
		entreprise.setId(entrepriseDto.getId());
		entreprise.setAddress(entrepriseDto.getAddress());
		entreprise.setTva(entrepriseDto.getTva());

		return entreprise;

	}

	public List<EntrepriseDto> mapEntitysToDtos(List<Entreprise> entreprises) {
		return entreprises.stream().map(dto -> mapEntityToDto(dto)).filter(dto -> dto != null)
				.collect(Collectors.toList());
	}

}
