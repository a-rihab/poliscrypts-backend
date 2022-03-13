package com.poliscrypts.dto;

import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;

import com.poliscrypts.util.ContactType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ContactDto {

	private Long id;

	@NotBlank(message = "Veuillez saisir le nom !")
	private String firstName;

	@NotBlank(message = "Veuillez saisir le prénom !")
	private String lastName;

	@NotBlank(message = "Veuillez saisir une address !")
	private String address;

	@Enumerated(EnumType.STRING)
	private ContactType type;

	private int tva;

	private List<Long> entreprises;

}
