package com.poliscrypts.dto;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ContactDto {

	private Long id;

	@NotBlank(message = "Veuillez saisir le nom !")
	private String firstName;

	@NotBlank(message = "Veuillez saisir le prénom !")
	private String lastName;

	@NotBlank(message = "Veuillez saisir une address !")
	private String address;

	private String type;

	private Integer tva;

	private Set<Long> enterprises = new HashSet<>();

}
