package com.poliscrypts.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poliscrypts.model.Enterprise;

public interface EnterpriseRepository extends JpaRepository<Enterprise, Long> {

}
