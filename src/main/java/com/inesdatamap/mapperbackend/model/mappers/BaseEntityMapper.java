package com.inesdatamap.mapperbackend.model.mappers;

import org.mapstruct.Mapper;

import com.inesdatamap.mapperbackend.model.dto.BaseEntityDTO;
import com.inesdatamap.mapperbackend.model.jpa.BaseEntity;

/**
 * Mapper interface for converting between BaseEntity and BaseEntityDTO.
 *
 */
@Mapper(componentModel = "spring")
public interface BaseEntityMapper {

	/**
	 * Maps an BaseEntity to an BaseEntityDTO.
	 *
	 * @param entity
	 *            the entity
	 * @return the corresponding BaseEntityDTO
	 */
	BaseEntityDTO entityToDto(BaseEntity entity);

}
