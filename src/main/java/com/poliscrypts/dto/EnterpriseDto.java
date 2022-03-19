package com.poliscrypts.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EnterpriseDto {

	private Long id;

	@NotBlank(message = "Please enter valid address !")
	private String address;

	@Min(value = 1, message = "Tva must be at leas")
	private Integer tva;

}
