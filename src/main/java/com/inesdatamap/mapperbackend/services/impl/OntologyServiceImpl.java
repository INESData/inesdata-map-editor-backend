package com.inesdatamap.mapperbackend.services.impl;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.StringDocumentSource;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
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
	 * Retrieves all class names from the ontology specified by the given ID.
	 *
	 * @param id
	 *            the unique identifier of the ontology entity
	 * @return a list of class names extracted from the ontology
	 * @throws OntologyParserException
	 *             if there is an error retrieving or processing the ontology, including errors from loading the ontology
	 */
	@Override
	public List<String> getOntologyClasses(Long id) {

		List<String> classList = new ArrayList<>();

		try {

			// Get entity from DB
			Ontology ontology = this.getEntity(id);

			// Read ontology file content
			String ontologyContent = this.getOntologyContent(ontology);

			// Create OWLOntologyManager instance and load the ontology
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			OWLOntology owl = manager.loadOntologyFromOntologyDocument(new StringDocumentSource(ontologyContent));

			// Get all classes in ontology and return list
			classList = this.getClasses(owl);

		} catch (OWLOntologyCreationException e) {
			throw new OntologyParserException("Failed getting classes from ontology: " + id, e);
		}

		return classList;

	}

	/**
	 * Retrieves all the class names from the provided OWL ontology.
	 *
	 * @param owl
	 *            the OWL ontology from which to retrieve class names
	 * @return a list of class names
	 * @throws OWLOntologyCreationException
	 *             if there is an error accessing or processing the ontology
	 */
	public List<String> getClasses(OWLOntology owl) throws OWLOntologyCreationException {

		// List to store the classes
		List<String> ontologyClasses = new ArrayList<>();

		// Iterate over all classes in the ontology
		for (OWLClass owlClass : owl.getClassesInSignature()) {
			// Extract the class name from its IRI fragment
			String className = owlClass.getIRI().getFragment();

			// Add the class name to the list
			if (className != null && !className.isEmpty()) {
				ontologyClasses.add(className);
			}
		}

		return ontologyClasses;
	}

	/**
	 * Retrieves a list of properties for a specified class from an ontology identified by its ID.
	 *
	 * @param id
	 *            The ID of the ontology entity to retrieve.
	 * @param className
	 *            The name of the class whose properties are to be retrieved.
	 * @return A list of properties for the specified class from the ontology.
	 *
	 */
	@Override
	public List<String> getClassProperties(Long id, String className) {

		// Get entity
		Ontology ontology = this.getEntity(id);

		// Read ontology file content
		String ontologyContent = this.getOntologyContent(ontology);

		try {
			return this.getProperties(ontologyContent, className);
		} catch (OWLOntologyCreationException e) {
			throw new OntologyParserException("Failed getting properties from ontology class: " + className, e);
		}
	}

	/**
	 * Retrieves the data properties associated with a specified class from the given ontology
	 *
	 * @param ontologyContent
	 *            The content of the ontology as a string
	 * @param className
	 *            The name of the class for which the data properties are to be retrieved
	 * @return A list of properties associated with the specified class
	 * @throws OWLOntologyCreationException
	 *             if there is an error during the ontology creation process
	 */
	public List<String> getProperties(String ontologyContent, String className) throws OWLOntologyCreationException {

		if (ontologyContent == null || ontologyContent.isEmpty()) {
			throw new IllegalArgumentException("Ontology content is empty.");
		}
		if (className == null || className.isEmpty()) {
			throw new IllegalArgumentException("Class name is empty.");
		}

		// Create OWLOntologyManager instance and load the ontology
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology owl = manager.loadOntologyFromOntologyDocument(new StringDocumentSource(ontologyContent));

		// Find the owl class by class name
		OWLClass owlClass = owl.classesInSignature().filter(clazz -> clazz.getIRI().getFragment().equals(className)).findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Class " + className + " not found in the ontology"));

		List<String> properties = new ArrayList<>();

		// Collect properties and add them in order
		List<String> dataProperties = this.getDataProperties(owlClass, owl);
		properties.addAll(dataProperties);
		List<String> objectProperties = this.getObjectProperties(owlClass, owl);
		properties.addAll(objectProperties);
		List<String> annotationProperties = this.getAnnotationProperties(owl);
		properties.addAll(annotationProperties);


		return properties;

	}

	/**
	 * Retrieves a list of data property names associated with a specific OWL class in a given ontology.
	 *
	 * @param owlClass
	 *            The OWLClass representing the class for which data properties are being retrieved
	 * @param ontology
	 *            The OWLOntology that contains the axioms and properties.
	 * @return A list of strings containing the ata properties associated with the specified class
	 */
	public List<String> getDataProperties(OWLClass owlClass, OWLOntology ontology) {

		List<String> dataProperties = new ArrayList<>();

		// Iterate through all the data property domain axioms
		for (OWLDataPropertyDomainAxiom domainAxiom : ontology.getAxioms(AxiomType.DATA_PROPERTY_DOMAIN)) {
			OWLClassExpression domainExpression = domainAxiom.getDomain();
			OWLDataProperty property = domainAxiom.getProperty().asOWLDataProperty();

			// Check if the domain of the axiom contains the class
			if (domainExpression.equals(owlClass) || domainExpression.getClassesInSignature().contains(owlClass)) {
				dataProperties.add(property.getIRI().getFragment());
			}
		}

		return dataProperties;
	}

	/**
	 * Retrieves a list of object properties whose domain contains the specified OWLClass.
	 *
	 * @param owlClass
	 *            The OWL class for which object properties are to be retrieved
	 * @param ontology
	 *            The OWLOntology in which the object property axioms will be searched
	 * @return A list of object properties
	 */
	public List<String> getObjectProperties(OWLClass owlClass, OWLOntology ontology) {

		List<String> objectProperties = new ArrayList<>();

		// Iterate through all the object property domain axioms
		for (OWLObjectPropertyDomainAxiom domainAxiom : ontology.getAxioms(AxiomType.OBJECT_PROPERTY_DOMAIN)) {
			OWLClassExpression domainExpression = domainAxiom.getDomain();
			OWLObjectProperty property = domainAxiom.getProperty().asOWLObjectProperty();

			// Check if the domain of the axiom contains the class
			if (domainExpression.equals(owlClass) || domainExpression.getClassesInSignature().contains(owlClass)) {
				objectProperties.add(property.getIRI().getFragment());
			}
		}
		return objectProperties;
	}

	/**
	 * Retrieves a list of annotation properties from the ontology
	 *
	 * @param ontology
	 *            the OWLOntology
	 * @return a list of annotation properties in the ontology
	 */
	public List<String> getAnnotationProperties(OWLOntology ontology) {

		List<String> annotationProperties = new ArrayList<>();

		// Get all annotation properties in the ontology
		ontology.annotationPropertiesInSignature().forEach(annotationProperty -> {
			annotationProperties.add(annotationProperty.getIRI().getFragment());
		});

		return annotationProperties;
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

		// Validate ontology is not null
		if (ontology.getContent() == null) {
			throw new IllegalArgumentException("Ontology has no content.");
		}

		// Get ontology bytes
		byte[] contentBytes = ontology.getContent();

		// Convert bytes to String
		return new String(contentBytes, StandardCharsets.UTF_8);
	}

}
