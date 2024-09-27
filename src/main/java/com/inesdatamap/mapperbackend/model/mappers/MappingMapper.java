package com.inesdatamap.mapperbackend.model.mappers;

import org.mapstruct.Mapper;

import com.inesdatamap.mapperbackend.model.dto.MappingDTO;
import com.inesdatamap.mapperbackend.model.jpa.Mapping;

/**
 * Mapper interface for converting between {@link com.inesdatamap.mapperbackend.model.jpa.Mapping} and {@link com.inesdatamap.mapperbackend.model.dto.MappingDTO}.
 */
@Mapper(componentModel = "spring", uses = MappingFieldMapper.class)
public interface MappingMapper extends BaseEntityMapper<MappingDTO, Mapping> {

}