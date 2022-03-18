package com.poliscrypts.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.poliscrypts.util.ContactType;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	private String address;

	@Enumerated(EnumType.STRING)
	private ContactType type;

	private int tva;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "contact_entreprises", joinColumns = {
			@JoinColumn(foreignKey = @ForeignKey(name = "fk_contact_id")) }, inverseJoinColumns = {
					@JoinColumn(foreignKey = @ForeignKey(name = "fk_entreprise_id")) })
	private List<Entreprise> entreprises = new ArrayList<>();

}