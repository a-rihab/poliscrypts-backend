package com.poliscrypts.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EnterpriseDto {

	private Long id;

	@NotBlank(message = "Address must not be empty !")
	private String address;

	@Min(value = 1, message = "Tva must be at least 1 !")
	private Integer tva;

}
