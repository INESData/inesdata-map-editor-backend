package com.inesdatamap.mapperbackend.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.StringDocumentSource;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import com.inesdatamap.mapperbackend.model.dto.PropertyDTO;
import com.inesdatamap.mapperbackend.model.enums.PropertyTypeEnum;

/**
 * Utility class for OWL.
 */
public final class OWLUtils {

	/**
	 * Private constructor to prevent instantiation.
	 */
	private OWLUtils() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Retrieves all the class names from the provided OWL ontology.
	 *
	 * @param owl
	 *            the OWL ontology from which to retrieve class names
	 * @return a list of class names
	 */
	public static List<String> getClasses(OWLOntology owl) {

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
	public static List<PropertyDTO> getProperties(String ontologyContent, String className) throws OWLOntologyCreationException {

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

		List<PropertyDTO> properties = new ArrayList<>();

		// Collect properties and add them
		properties.addAll(getDataProperties(owlClass, owl));
		properties.addAll(getObjectProperties(owlClass, owl));
		properties.addAll(getAnnotationProperties(owl));

		return properties;

	}

	/**
	 * Retrieves a list of data properties associated with a specified class in the ontology
	 *
	 * @param owlClass
	 *            The OWL class
	 * @param ontology
	 *            The ontology in which to search for the data properties
	 *
	 * @return A list of data properties
	 */
	public static List<PropertyDTO> getDataProperties(OWLClass owlClass, OWLOntology ontology) {

		Set<PropertyDTO> dataProperties = new HashSet<>();

		// Iterate through all the data property domain axioms
		for (OWLDataPropertyDomainAxiom dataDomainAxiom : ontology.getAxioms(AxiomType.DATA_PROPERTY_DOMAIN)) {
			OWLClassExpression domain = dataDomainAxiom.getDomain();
			OWLDataProperty property = dataDomainAxiom.getProperty().asOWLDataProperty();

			// Check if the domain of the axiom contains the class
			if (domain.equals(owlClass) || domain.getClassesInSignature().contains(owlClass) && property != null) {
				dataProperties.add(createPropertyDTO(property.getIRI().getFragment(), PropertyTypeEnum.DATA));
			}
		}

		// Iterate through all the data property range axioms
		for (OWLDataPropertyRangeAxiom dataRangeAxiom : ontology.getAxioms(AxiomType.DATA_PROPERTY_RANGE)) {
			OWLDataRange range = dataRangeAxiom.getRange();
			OWLDataProperty property = dataRangeAxiom.getProperty().asOWLDataProperty();

			// Check if the range of the axiom contains the class
			if (range.getClassesInSignature().contains(owlClass) && property != null) {
				dataProperties.add(createPropertyDTO(property.getIRI().getFragment(), PropertyTypeEnum.DATA));
			}
		}

		return new ArrayList<>(dataProperties);
	}

	/**
	 * Retrieves a list of object properties associated with a specified class in the ontology
	 *
	 * @param owlClass
	 *            The OWL class
	 * @param ontology
	 *            The ontology in which to search for the object properties
	 *
	 * @return A list of object properties
	 */
	public static List<PropertyDTO> getObjectProperties(OWLClass owlClass, OWLOntology ontology) {

		Set<PropertyDTO> objectProperties = new HashSet<>();

		// Iterate through all the object property domain axioms
		for (OWLObjectPropertyDomainAxiom domainAxiom : ontology.getAxioms(AxiomType.OBJECT_PROPERTY_DOMAIN)) {
			OWLClassExpression domainExpression = domainAxiom.getDomain();
			OWLObjectProperty property = domainAxiom.getProperty().asOWLObjectProperty();

			// Check if the domain of the axiom contains the class
			if (domainExpression.equals(owlClass) || domainExpression.getClassesInSignature().contains(owlClass) && property != null) {
				objectProperties.add(createPropertyDTO(property.getIRI().getFragment(), PropertyTypeEnum.OBJECT));
			}
		}

		// Iterate through all the object property range axioms
		for (OWLObjectPropertyRangeAxiom rangeAxiom : ontology.getAxioms(AxiomType.OBJECT_PROPERTY_RANGE)) {
			OWLClassExpression rangeExpression = rangeAxiom.getRange();
			OWLObjectProperty property = rangeAxiom.getProperty().asOWLObjectProperty();

			// Check if the range of the axiom contains the class
			if (rangeExpression.equals(owlClass) || rangeExpression.getClassesInSignature().contains(owlClass) && property != null) {
				objectProperties.add(createPropertyDTO(property.getIRI().getFragment(), PropertyTypeEnum.OBJECT));
			}
		}
		return new ArrayList<>(objectProperties);
	}

	/**
	 * Retrieves a list of annotation properties in the ontology
	 *
	 * @param ontology
	 *            the OWLOntology
	 * @return A list of annotation properties
	 */
	public static List<PropertyDTO> getAnnotationProperties(OWLOntology ontology) {

		List<PropertyDTO> annotationProperties = new ArrayList<>();

		// Get all annotation properties in the ontology
		ontology.annotationPropertiesInSignature().forEach(annotationProperty -> annotationProperties
				.add(createPropertyDTO(annotationProperty.getIRI().getFragment(), PropertyTypeEnum.ANNOTATION)));

		return annotationProperties;
	}

	/**
	 * Creates a new PropertyDTO object with the specified property name and type.
	 *
	 * @param property
	 *            the name
	 * @param propertyEnum
	 *            the type
	 * @return a PropertyDTO
	 */
	public static PropertyDTO createPropertyDTO(String property, PropertyTypeEnum propertyEnum) {
		PropertyDTO propertyDTO = new PropertyDTO();
		propertyDTO.setPropertyType(propertyEnum);
		propertyDTO.setName(property);
		return propertyDTO;
	}

}
