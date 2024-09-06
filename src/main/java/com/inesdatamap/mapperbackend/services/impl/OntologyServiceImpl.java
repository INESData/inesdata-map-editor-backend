package com.inesdatamap.mapperbackend.services.impl;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.StringDocumentSource;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.inesdatamap.mapperbackend.exceptions.OntologyParserException;
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
	 *            the ID of the ontology
	 * @return a list of class names extracted from the ontology
	 */
	@Override
	public List<String> getOntologyClasses(Long id) {

		// Get entity from DB
		Ontology ontology = this.getEntity(id);

		// Read ontology file content
		String ontologyContent = this.getOntologyContent(ontology);

		// Get all classes in ontology and return list
		try {
			return this.getClasses(ontologyContent);
		} catch (OWLOntologyCreationException e) {
			throw new OntologyParserException("Failed getting classes from ontology: " + ontology.getName(), e);
		}
	}

	/**
	 * Retrieves a list of attributes for a specified class from an ontology identified by its ID.
	 *
	 * @param id
	 *            The ID of the ontology entity to retrieve.
	 * @param className
	 *            The name of the class whose attributes are to be retrieved.
	 * @return A list of attributes for the specified class from the ontology.
	 *
	 */
	@Override
	public List<String> getOntologyAttributes(Long id, String className) {

		// Get entity
		Ontology ontology = this.getEntity(id);

		// Read ontology file content
		String ontologyContent = this.getOntologyContent(ontology);

		try {
			return this.getAttributes(ontologyContent, className);
		} catch (OWLOntologyCreationException e) {
			throw new OntologyParserException("Failed getting attributes from ontology: " + ontology.getName(), e);
		}
	}

	/**
	 * Retrieves the content of the ontology as a string. Converts the byte array content stored in the ontology to its original string
	 * representation using UTF-8 encoding.
	 *
	 * @param ontology
	 *            the Ontology entity containing the content as a byte array
	 * @return the content of the ontology as a string in UTF-8 format
	 */
	public String getOntologyContent(Ontology ontology) {

		// Validate that the ontology is not null
		if (ontology.getContent() == null) {
			throw new IllegalArgumentException("Ontology has no content.");
		}

		// Get ontology bytes
		byte[] contentBytes = ontology.getContent();

		// Convert bytes to String
		return new String(contentBytes, StandardCharsets.UTF_8);
	}

	/**
	 * Retrieves all class names (local names) from an ontology provided as a string.
	 *
	 * This method parses the ontology content from a string, loads it into an OWLOntology object, and extracts the local names of all
	 * classes defined in the ontology.
	 *
	 * @param ontologyContent
	 *            a string containing the ontology data in a format supported by OWL API
	 * @return a list of class names as strings
	 * @throws OWLOntologyCreationException
	 *             if there is an error during the ontology creation process
	 */
	public List<String> getClasses(String ontologyContent) throws OWLOntologyCreationException {

		if (ontologyContent == null || ontologyContent.isEmpty()) {
			throw new IllegalArgumentException("Ontology content is empty.");
		}

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology owl = manager.loadOntologyFromOntologyDocument(new StringDocumentSource(ontologyContent));

		// Create a list to store the classes as String
		List<String> classList = new ArrayList<>();

		// Check if the ontology contains classes
		if (!owl.classesInSignature().iterator().hasNext()) {
			return classList;
		}

		// Iterating over all classes in the ontology
		owl.classesInSignature().forEach(owlClass -> {

			String className = owlClass.getIRI().getFragment();

			// Add the class to the list
			classList.add(className);
		});

		// Return list with all classes
		return classList;
	}

	/**
	 * Extracts and returns a list of attributes for a specified class from an ontology represented by the given ontology content.
	 *
	 * @param ontologyContent
	 *            The content of the ontology as a string.
	 * @param className
	 *            The name of the class whose attributes are to be extracted.
	 * @return A list of attributes for the specified class.
	 * @throws OWLOntologyCreationException
	 *             if there is an error during the ontology creation process
	 */
	public List<String> getAttributes(String ontologyContent, String className) throws OWLOntologyCreationException {

		List<String> properties = new ArrayList<>();

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology owl = manager.loadOntologyFromOntologyDocument(new StringDocumentSource(ontologyContent));

		// Validate that class name is not null or empty
		if (className == null || className.isEmpty()) {
			throw new IllegalArgumentException("No class name selected.");
		}

		// Iterate over classes in the ontology and find the one that matches className
		owl.classesInSignature().forEach(clazz -> {
			// Get properties of the matching class
			List<String> classProperties = this.getIndividuals(clazz, className, owl);

			// Add the properties to the main list
			properties.addAll(classProperties);
		});

		return properties;

	}

	/**
	 * Retrieves the data properties associated with a specified class from the given ontology.
	 *
	 * @param clazz
	 *            The OWL class whose properties are to be retrieved.
	 * @param className
	 *            The name of the class to match against the provided OWL class.
	 * @param owl
	 *            The OWL ontology from which to retrieve the properties.
	 * @return A list of data property names (fragments) associated with the specified class.
	 */
	public List<String> getIndividuals(OWLClass clazz, String className, OWLOntology owl) {

		// Get class name from clazz
		String classFragment = clazz.getIRI().getFragment();

		if (classFragment == null || classFragment.isEmpty()) {
			throw new IllegalArgumentException("There is no class in the ontology.");
		}

		List<String> properties = new ArrayList<>();
		// Check if ontology contains received class name
		if (classFragment.equals(className)) {

			// Find data properties for class
			Set<OWLDataProperty> dataProperties = owl.dataPropertiesInSignature().collect(Collectors.toSet());
			for (OWLDataProperty dataProperty : dataProperties) {

				// Check if the class is a domain of the data property
				boolean isDomain = EntitySearcher.getDomains(dataProperty, owl).anyMatch(domain -> domain.equals(clazz));

				if (isDomain) {
					properties.add(dataProperty.getIRI().getFragment());
				}
			}
		}
		return properties;
	}

}
