package com.inesdatamap.mapperbackend.services.impl;

import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.inesdatamap.mapperbackend.model.dto.OntologyDTO;
import com.inesdatamap.mapperbackend.model.jpa.Ontology;
import com.inesdatamap.mapperbackend.model.mappers.OntologyMapper;
import com.inesdatamap.mapperbackend.repositories.jpa.OntologyRepository;
import com.inesdatamap.mapperbackend.services.OntologyService;

/**
 * Implementation of the OntologyService interface.
 *
 */
@Service
public class OntologyServiceImpl implements OntologyService {

	private OntologyRepository ontologyRepo;

	private OntologyMapper ontologyMapper;

	/**
	 * Constructor with dependencies injected.
	 *
	 * @param ontologyRepo
	 *            the ontology repository
	 * @param ontologyMapper
	 *            the ontology mapper
	 */
	public OntologyServiceImpl(OntologyRepository ontologyRepo, OntologyMapper ontologyMapper) {
		this.ontologyRepo = ontologyRepo;
		this.ontologyMapper = ontologyMapper;
	}

	/**
	 * Retrieves a list of all ontologies and maps them to their corresponding DTOs.
	 *
	 * @return List of OntologyDTOs
	 */
	@Override
	public Page<OntologyDTO> listOntologies(Pageable pageable) {

		Page<Ontology> ontologieList = this.ontologyRepo.findAll(pageable);

		return ontologieList.map(this.ontologyMapper::entityToDto);

	}

	/**
	 * Updates an ontology by its ID.
	 *
	 * @param id
	 *            the ID of the ontology to update
	 * @param ontologyDto
	 *            the OntologyDTO
	 * @return the updated ontology
	 */
	@Override
	public OntologyDTO updateOntology(Long id, OntologyDTO ontologyDto) {

		// Get DB entity
		Ontology ontologyDB = this.getEntity(id); // existing

		// New ontology to save
		Ontology ontologySource = this.ontologyMapper.dtoToEntity(ontologyDto); // source

		// Updated ontology
		Ontology ontologyUpdated = this.ontologyRepo.saveAndFlush(this.ontologyMapper.merge(ontologySource, ontologyDB));

		return this.ontologyMapper.entityToDto(ontologyUpdated);

	}

	/**
	 * Retrieves an ontology entity by its ID.
	 *
	 * @param id
	 *            the ID of the ontology to retrieve
	 * @return the ontology entity corresponding to the given ID
	 */
	@Override
	public Ontology getEntity(Long id) {
		return this.ontologyRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Entity not found with id: " + id));
	}

}
