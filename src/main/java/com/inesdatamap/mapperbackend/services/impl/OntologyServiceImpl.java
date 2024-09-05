package com.inesdatamap.mapperbackend.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.inesdatamap.mapperbackend.model.dto.OntologyDTO;
import com.inesdatamap.mapperbackend.model.dto.SearchOntologyDTO;
import com.inesdatamap.mapperbackend.model.jpa.Ontology;
import com.inesdatamap.mapperbackend.model.mappers.OntologyMapper;
import com.inesdatamap.mapperbackend.repositories.jpa.OntologyRepository;
import com.inesdatamap.mapperbackend.services.OntologyService;
import com.inesdatamap.mapperbackend.utils.FileUtils;

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
	 * @return the updated ontology
	 */
	@Override
	public OntologyDTO updateOntology(Long id, OntologyDTO ontologyDto) {

		if (ontologyDto == null) {
			throw new IllegalArgumentException("The ontology has no data to update");
		}

		// Get DB entity
		Ontology ontologyDB = this.getEntity(id);

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

		if (ontologyDto == null) {
			throw new IllegalArgumentException("The ontology has no data");
		}

		// DTO to entity
		Ontology ontology = this.ontologyMapper.dtoToEntity(ontologyDto);

		if (file != null && !file.isEmpty()) {

			// Read file content
			FileUtils.processFileContent(file, ontology);

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
		return this.ontologyRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id.toString()));
	}

	@Override
	public List<SearchOntologyDTO> getOntologies(Sort nameSort) {

		List<Ontology> ontologiesList = this.ontologyRepo.findAll(nameSort);

		return this.ontologyMapper.entitytoSearchOntologyDTO(ontologiesList);

	}

	/**
	 * Retrieves all class names from an ontology specified by its ID.
	 *
	 * @param id
	 *            the ID of the ontology entity to be retrieved from the database
	 * @return a list of class names extracted from the ontology
	 */
	@Override
	public List<String> getOntologyClasses(Long id) {

		// Get entity from DB
		Ontology ontology = this.getEntity(id);

		// Read ontology file content
		String ontologyContent = FileUtils.getOntologyContent(ontology);

		// Get all classes in ontology and return list
		return FileUtils.getClasses(ontologyContent);
	}

}
