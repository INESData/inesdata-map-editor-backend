package com.inesdatamap.mapperbackend.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.inesdatamap.mapperbackend.model.dto.SearchOntologyDTO;
import com.inesdatamap.mapperbackend.model.jpa.Ontology;

/**
 * Service interface for managing ontologies.
 */

public interface OntologyService {

	/**
	 * Gets entity by its id.
	 *
	 * @param id
	 *            the ID of the ontology to get
	 * @return Ontology
	 */
	Ontology getEntity(Long id);

	/**
	 * Retrieves all ontologies.
	 *
	 * @param pageable
	 *            pageable
	 *
	 * @return List of ontologies
	 */
	Page<SearchOntologyDTO> listOntologies(Pageable pageable);

	/**
	 * Updates an ontology identified by its ID.
	 *
	 * @param id
	 *            the ID of the ontology to update
	 * @param searchOntologyDto
	 *            the OntologyDTO
	 * @return the updated ontology
	 */
	SearchOntologyDTO updateOntology(Long id, SearchOntologyDTO searchOntologyDto);

}
