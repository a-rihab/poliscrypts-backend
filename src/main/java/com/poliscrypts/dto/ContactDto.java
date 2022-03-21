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

	@NotBlank(message = "First name must not be empty !")
	private String firstName;

	@NotBlank(message = "Last name must not be empty !")
	private String lastName;

	@NotBlank(message = "Address must not be empty !")
	private String address;

	private String type;

	private Integer tva;

	private Set<Long> enterprises = new HashSet<>();

}
