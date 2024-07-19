package com.inesdatamap.mapperbackend.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.inesdatamap.mapperbackend.model.dto.OntologyDTO;
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
	Page<OntologyDTO> listOntologies(Pageable pageable);

	/**
	 * Updates an ontology identified by its ID.
	 *
	 * @param id
	 *            the ID of the ontology to update
	 * @param ontologyDto
	 *            the OntologyDTO
	 * @return the updated ontology
	 */
	OntologyDTO updateOntology(Long id, OntologyDTO ontologyDto);

}
