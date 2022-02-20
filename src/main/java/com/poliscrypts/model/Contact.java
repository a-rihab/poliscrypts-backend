package com.poliscrypts.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import com.poliscrypts.util.ContactType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Veuillez saisir le nom !")
	@Column(name = "first_name")
	private String firstName;

	@NotBlank(message = "Veuillez saisir le prénom !")
	@Column(name = "last_name")
	private String lastName;

	@NotBlank(message = "Veuillez saisir une address !")
	private String address;

	@NotBlank(message = "Veuillez selectionner le type !")
	@Enumerated(EnumType.STRING)
	private ContactType type;

	private int tva;

	@OneToMany
	@JoinColumn(name = "fk_contact_id")
	private List<Entreprise> entreprises;

}
