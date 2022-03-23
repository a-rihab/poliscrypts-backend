package com.poliscrypts.mapper;

import org.springframework.stereotype.Component;

import com.poliscrypts.dto.EnterpriseDto;
import com.poliscrypts.model.Enterprise;

@Component
public class EnterpriseMapper extends CommonMapper<Enterprise, EnterpriseDto> {

	@Override
	protected EnterpriseDto mapEntityToDto(Enterprise enterprise) {

		EnterpriseDto enterpriseDto = new EnterpriseDto();

		enterpriseDto.setId(enterprise.getId());
		enterpriseDto.setAddress(enterprise.getAddress());
		enterpriseDto.setTva(enterprise.getTva());

		return enterpriseDto;
	}

	@Override
	protected Enterprise mapDtoToEntity(EnterpriseDto enterpriseDto) {

		Enterprise enterprise = new Enterprise();
		enterprise.setId(enterpriseDto.getId());
		enterprise.setAddress(enterpriseDto.getAddress());
		enterprise.setTva(enterpriseDto.getTva());

		return enterprise;
	}

}
