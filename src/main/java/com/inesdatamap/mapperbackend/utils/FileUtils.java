package com.inesdatamap.mapperbackend.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.StringDocumentSource;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.springframework.web.multipart.MultipartFile;

import com.inesdatamap.mapperbackend.exceptions.FileCreationException;
import com.inesdatamap.mapperbackend.model.enums.DataFileTypeEnum;
import com.inesdatamap.mapperbackend.model.jpa.Ontology;

/**
 * Utility for file validation and file
 *
 * @author gmv
 */
public final class FileUtils {

	// Private constructor to prevent instantiation
	private FileUtils() {

	}

	/**
	 * Validates the file extension against the supported extensions defined in the DataFileTypeEnum.
	 *
	 * @param fileExtension
	 * 	the MIME type to validate
	 *
	 * @throws IllegalArgumentException
	 * 	if the provided file extension is not supported
	 */
	public static void validateFileExtension(String fileExtension) {
		if (!DataFileTypeEnum.isValidExtension(fileExtension)) {
			throw new IllegalArgumentException("Unsupported file extension: " + fileExtension);
		}
	}

	/**
	 * Processes the content of the MultipartFile and sets it as the content of the specified Ontology.
	 *
	 * @param file
	 * 	the MultipartFile containing the content to be processed
	 * @param ontology
	 * 	the Ontology entity where the file content will be stored
	 *
	 * @throws UncheckedIOException
	 * 	if an error occurs while reading the file content.
	 */
	public static void processFileContent(MultipartFile file, Ontology ontology) {

		try {
			// Convert the file content to a byte array
			byte[] fileContent = file.getBytes();

			// Set the byte array as the content of the ontology
			ontology.setContent(fileContent);

		} catch (IOException e) {
			throw new UncheckedIOException("Failed to store file content", e);
		}
	}

	/**
	 * Reads the first line of the given MultipartFile to extract headers and sets them in the provided FileSource object.
	 *
	 * @param file
	 * 	the MultipartFile from which to read the headers
	 *
	 * @return the headers read from the file
	 *
	 * @throws UncheckedIOException
	 * 	if an error occurs while reading the file content
	 */
	public static String processFileHeaders(MultipartFile file) {
		try {
			// Read file content
			BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));

			// Read file's first line with headers
			return reader.readLine();

		} catch (IOException e) {
			throw new UncheckedIOException("Failed to store file headers", e);
		}
	}

	/**
	 * Saves the given MultipartFile to the specified path.
	 *
	 * @param file
	 * 	the MultipartFile to save
	 * @param path
	 * 	the path where the file will be saved
	 */
	public static void saveFile(MultipartFile file, String path) {

		try {

			Files.createDirectories(Paths.get(path));
			file.transferTo(new File(path, Objects.requireNonNull(FilenameUtils.getName(file.getOriginalFilename()))));

		} catch (IOException e) {
			throw new FileCreationException("Failed to save file: " + path, e);
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
	public static String getOntologyContent(Ontology ontology) {

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
	public static List<String> getClasses(String ontologyContent) throws OWLOntologyCreationException {

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
	public static List<String> getAttributes(String ontologyContent, String className) throws OWLOntologyCreationException {

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
			List<String> classProperties = getIndividuals(clazz, className, owl);

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
	public static List<String> getIndividuals(OWLClass clazz, String className, OWLOntology owl) {

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
