package com.inesdatamap.mapperbackend.model.mappers;

import org.mapstruct.Mapper;

import com.inesdatamap.mapperbackend.model.dto.PredicateObjectMapDTO;
import com.inesdatamap.mapperbackend.model.jpa.PredicateObjectMap;

/**
 * Mapper interface for converting between {@link PredicateObjectMapMapper} and {@link com.inesdatamap.mapperbackend.model.dto.PredicateObjectMapDTO}.
 */
@Mapper(componentModel = "spring", uses = MappingFieldMapper.class)
public interface PredicateObjectMapMapper extends BaseEntityMapper<PredicateObjectMapDTO, PredicateObjectMap> {

}
