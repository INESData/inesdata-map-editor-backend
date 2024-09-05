package com.inesdatamap.mapperbackend.model.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.inesdatamap.mapperbackend.model.dto.OntologyDTO;
import com.inesdatamap.mapperbackend.model.dto.SearchOntologyDTO;
import com.inesdatamap.mapperbackend.model.jpa.Ontology;

/**
 * Mapper interface for converting between Ontology and OntologyDTO.
 *
 */
@Mapper(componentModel = "spring", uses = BaseEntityMapper.class)
public interface OntologyMapper extends BaseEntityMapper<OntologyDTO, Ontology> {

	/**
	 * Converts an ontology entity to a SearchOntologyDTO DTO.
	 *
	 * @param ontology
	 *            the ontology entity to be converted
	 * @return the corresponding SearchOntologyDTO DTO
	 */
	SearchOntologyDTO entitytoSearchOntologyDTO(Ontology ontology);

	/**
	 * Converts a list of Ontology entities to a list of SearchOntologyDTO DTOs.
	 *
	 * @param ontologiesList
	 *            the list of Ontology entities to be converted
	 * @return a list of SearchOntologyDTO objects corresponding to the input entities
	 */
	List<SearchOntologyDTO> entitytoSearchOntologyDTO(List<Ontology> ontologiesList);

}
