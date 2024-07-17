package com.inesdatamap.mapperbackend.model.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.inesdatamap.mapperbackend.model.dto.OntologyDTO;
import com.inesdatamap.mapperbackend.model.jpa.Ontology;

/**
 * Mapper interface for converting between Ontology and OntologyDTO.
 *
 */
@Mapper(componentModel = "spring", uses = BaseEntityMapper.class)
public interface OntologyMapper {

	/**
	 * Maps an Ontology entity to an OntologyDTO.
	 *
	 * @param ontology
	 *            the Ontology entity
	 * @return the corresponding OntologyDTO
	 */
	@Mapping(target = "id", source = "id")
	@Mapping(target = "version", source = "version")
	OntologyDTO entityToDto(Ontology ontology);

	/**
	 * Maps an OntologyDTO to an Ontology.
	 *
	 * @param ontology
	 *            the OntologyDTO
	 * @return the corresponding ontology
	 */
	@Mapping(target = "id", source = "id")
	@Mapping(target = "version", source = "version")
	Ontology dtoToEntity(OntologyDTO ontology);

	/**
	 * Converts a list of Ontology entities to a list of OntologyDTOs.
	 *
	 * @param ontologies
	 *            list of Ontology entities to convert
	 * @return list of OntologyDTO objects
	 */
	List<OntologyDTO> entitiesToDtos(List<Ontology> ontologies);

}
