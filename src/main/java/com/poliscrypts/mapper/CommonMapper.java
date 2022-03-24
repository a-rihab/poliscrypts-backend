package com.poliscrypts.mapper;

import java.util.List;
import java.util.stream.Collectors;

public abstract class CommonMapper<CommonObject, CommonDto> {

	protected abstract CommonDto mapEntityToDto(CommonObject entity);

	protected abstract CommonObject mapDtoToEntity(CommonDto dto);

	public CommonDto entityToDto(CommonObject entity) {

		if (entity == null) {
			return null;
		}

		CommonDto dto = mapEntityToDto(entity);

		return dto;
	}

	public CommonObject dtoToEntity(CommonDto dto) {

		CommonObject entity = mapDtoToEntity(dto);
		
		if (entity == null) {
			return null;
		}

		return entity;
	}

	public List<CommonObject> dtosToEntitys(List<CommonDto> dtos) {
		return dtos.stream().map(dto -> dtoToEntity(dto)).filter(e -> e != null).collect(Collectors.toList());
	}

	public List<CommonDto> entitysToDtos(List<CommonObject> entitys) {
		return entitys.stream().map(dto -> entityToDto(dto)).filter(dto -> dto != null).collect(Collectors.toList());
	}

}
