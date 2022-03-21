package com.poliscrypts.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.poliscrypts.model.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {
	Page<Contact> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrAddressContainingIgnoreCase(
			String firstName, String lastName, String address, Pageable pageable);
}
