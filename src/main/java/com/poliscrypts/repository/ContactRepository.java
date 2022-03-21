package com.poliscrypts.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poliscrypts.model.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {

}
