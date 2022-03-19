package com.poliscrypts.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.poliscrypts.model.Enterprise;

public interface EnterpriseRepository extends JpaRepository<Enterprise, Long> {

	Page<Enterprise> findByAddressContainingIgnoreCase(String address, Pageable pageable);

}
