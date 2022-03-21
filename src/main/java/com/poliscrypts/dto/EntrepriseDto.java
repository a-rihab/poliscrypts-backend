package com.poliscrypts.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EntrepriseDto {

	private Long id;

	@NotBlank(message = "Veuillez saisir une address !")
	private String address;

	@Min(value = 1, message = "Tva doit être min 1")
	private int tva;

}
