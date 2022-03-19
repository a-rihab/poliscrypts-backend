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

	@NotBlank(message = "Please enter valid firstName !")
	private String firstName;

	@NotBlank(message = "Please enter valid lastName !")
	private String lastName;

	@NotBlank(message = "Please enter valid address !")
	private String address;

	private String type;

	private Integer tva;

	private Set<Long> enterprises = new HashSet<>();

}
