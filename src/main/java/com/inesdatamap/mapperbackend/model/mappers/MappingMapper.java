package com.inesdatamap.mapperbackend.model.mappers;

import org.mapstruct.Mapper;

import com.inesdatamap.mapperbackend.model.dto.MappingDTO;
import com.inesdatamap.mapperbackend.model.jpa.Mapping;

/**
 * Mapper interface for converting between Mapping and MappingDTO.
 *
 */
@Mapper(componentModel = "spring", uses = BaseEntityMapper.class)
public interface MappingMapper extends BaseEntityMapper<MappingDTO, Mapping> {

}
