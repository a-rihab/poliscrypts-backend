package com.poliscrypts.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.poliscrypts.model.Entreprise;

public interface EntrepriseRepository extends JpaRepository<Entreprise, Long> {

	Page<Entreprise> findByAddressContainingIgnoreCase(String address, Pageable pageable);

}
