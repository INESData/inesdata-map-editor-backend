package com.inesdatamap.mapperbackend.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.StringDocumentSource;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.springframework.web.multipart.MultipartFile;

import com.inesdatamap.mapperbackend.model.enums.DataFileTypeEnum;
import com.inesdatamap.mapperbackend.model.jpa.FileSource;
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
	 *            the MIME type to validate
	 * @throws IllegalArgumentException
	 *             if the provided file extension is not supported
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
	 *            the MultipartFile containing the content to be processed
	 * @param ontology
	 *            the Ontology entity where the file content will be stored
	 *
	 * @throws UncheckedIOException
	 *             if an error occurs while reading the file content.
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
	 *            the MultipartFile from which to read the headers
	 * @param fileSource
	 *            the FileSource entity where the headers and file name will be stored
	 * @throws UncheckedIOException
	 *             if an error occurs while reading the file content
	 */
	public static void processFileHeaders(MultipartFile file, FileSource fileSource) {
		try {
			// Read file content
			BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));

			// Read file's first line with headers
			String headers = reader.readLine();

			// Set headers and file name in FileSource
			fileSource.setFields(headers);
			fileSource.setFileName(file.getOriginalFilename());

		} catch (IOException e) {
			throw new UncheckedIOException("Failed to store file headers", e);
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
	 * @throws IllegalArgumentException
	 *             if there is an error while creating or loading the ontology from the string content
	 */
	public static List<String> getClasses(String ontologyContent) throws IllegalArgumentException {

		try {
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			OWLOntology owl = manager.loadOntologyFromOntologyDocument(new StringDocumentSource(ontologyContent));

			// Create a list to store the classes as String
			List<String> classList = new ArrayList<>();

			// Iterating over all classes in the ontology
			owl.classesInSignature().forEach(owlClass -> {

				String className = owlClass.getIRI().getFragment();
				// If there's no fragment, get name after last /
				if (className == null) {
					String iri = owlClass.getIRI().toString();
					className = iri.substring(iri.lastIndexOf('/') + 1);
				}

				// Add the class to the list
				classList.add(className);
			});

			// Return list with all classes
			return classList;
		} catch (OWLOntologyCreationException e) {
			throw new IllegalArgumentException("The ontology could not be loaded or is invalid.", e);
		}
	}

}
