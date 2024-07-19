package com.inesdatamap.mapperbackend.model.mappers;

import org.mapstruct.Mapper;

import com.inesdatamap.mapperbackend.model.dto.OntologyDTO;
import com.inesdatamap.mapperbackend.model.jpa.Ontology;

/**
 * Mapper interface for converting between Ontology and OntologyDTO.
 *
 */
@Mapper(componentModel = "spring", uses = BaseEntityMapper.class)
public interface OntologyMapper extends BaseEntityMapper<OntologyDTO, Ontology> {

}
