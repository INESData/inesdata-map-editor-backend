package com.inesdatamap.mapperbackend.services.impl;

import java.io.IOException;
import java.io.UncheckedIOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.inesdatamap.mapperbackend.model.dto.OntologyDTO;
import com.inesdatamap.mapperbackend.model.dto.SearchOntologyDTO;
import com.inesdatamap.mapperbackend.model.jpa.Ontology;
import com.inesdatamap.mapperbackend.model.mappers.OntologyMapper;
import com.inesdatamap.mapperbackend.repositories.jpa.OntologyRepository;
import com.inesdatamap.mapperbackend.services.OntologyService;

import jakarta.persistence.EntityNotFoundException;

/**
 * Implementation of the OntologyService interface.
 *
 */
@Service
public class OntologyServiceImpl implements OntologyService {

	@Autowired
	private OntologyRepository ontologyRepo;

	@Autowired
	private OntologyMapper ontologyMapper;

	/**
	 * Retrieves a list of all ontologies and maps them to their corresponding DTOs.
	 *
	 * @return List of OntologyDTOs
	 */
	@Override
	public Page<SearchOntologyDTO> listOntologies(Pageable pageable) {

		Page<Ontology> ontologiesList = this.ontologyRepo.findAll(pageable);

		return ontologiesList.map(this.ontologyMapper::entitytoSearchOntologyDTO);

	}

	/**
	 * Updates an ontology by its ID.
	 *
	 * @param id
	 *            the ID of the ontology to update
	 * @param ontologyDto
	 *            the OntologyDTO
	 * @param file
	 *            file content to update
	 * @return the updated ontology
	 */
	@Override
	public OntologyDTO updateOntology(Long id, OntologyDTO ontologyDto, MultipartFile file) {

		// Get DB entity
		Ontology ontologyDB = this.getEntity(id);

		if (file != null && !file.isEmpty()) {
			try {
				// Convert the file content to a byte array
				byte[] fileContent = file.getBytes();

				// Set the byte array as the content of the ontology
				ontologyDB.setContent(fileContent);
			} catch (IOException e) {
				throw new UncheckedIOException("Failed to store file content", e);
			}
		}

		// New ontology to save
		Ontology ontologySource = this.ontologyMapper.dtoToEntity(ontologyDto);

		// Updated ontology
		Ontology ontologyUpdated = this.ontologyRepo.saveAndFlush(this.ontologyMapper.merge(ontologySource, ontologyDB));

		return this.ontologyMapper.entityToDto(ontologyUpdated);

	}

	/**
	 * Deletes an ontology by its id.
	 *
	 * @param id
	 *            the ID of the ontology to delete
	 */
	@Override
	public void deleteOntology(Long id) {

		// Get entity if exists
		this.getEntity(id);

		this.ontologyRepo.deleteById(id);

	}

	/**
	 * Saves an ontology
	 *
	 * @param ontologyDto
	 *            the OntologyDTO
	 * @return the saved ontology
	 */
	@Override
	public OntologyDTO createOntology(OntologyDTO ontologyDto, MultipartFile file) {

		// DTO to entity
		Ontology ontology = this.ontologyMapper.dtoToEntity(ontologyDto);

		if (file != null && !file.isEmpty()) {
			try {
				// Convert the file content to a byte array
				byte[] fileContent = file.getBytes();

				// Set the byte array as the content of the ontology
				ontology.setContent(fileContent);
			} catch (IOException e) {
				throw new UncheckedIOException("Failed to store file content", e);
			}
		}

		// Save new entity
		Ontology savedOntology = this.ontologyRepo.save(ontology);

		return this.ontologyMapper.entityToDto(savedOntology);

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
		return this.ontologyRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found with id" + id.toString()));
	}

}
