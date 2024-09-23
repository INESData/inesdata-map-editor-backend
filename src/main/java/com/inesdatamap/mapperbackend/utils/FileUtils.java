package com.inesdatamap.mapperbackend.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import com.inesdatamap.mapperbackend.exceptions.FileCreationException;
import com.inesdatamap.mapperbackend.exceptions.FileDeleteException;
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
	 * Creates a file with the specified content.
	 *
	 * @param content
	 * 	the content of the file
	 * @param path
	 * 	the path where the file will be created
	 *
	 * @return the file
	 */
	public static File createFile(byte[] content, String path) {
		try {

			Path filePath = Paths.get(path);
			Files.createDirectories(filePath.getParent());
			Path file = Files.createFile(filePath);
			Files.write(file, content);

			return file.toFile();

		} catch (IOException e) {
			throw new FileCreationException("Failed to create file", e);
		}
	}

	/**
	 * Deletes the file at the specified path.
	 *
	 * @param path
	 * 	the path of the file to delete
	 */
	public static void deleteFile(Path path) {

		if (Files.notExists(path)) {
			return;
		}

		try {
			Files.delete(path);
		} catch (IOException e) {
			throw new FileDeleteException("Failed to delete file: " + path, e);
		}
	}

	/**
	 * Creates the directories at the specified path.
	 *
	 * @param path
	 * 	the path
	 */
	public static void createDirectories(Path path) {

		try {
			Files.createDirectories(path);
		} catch (IOException e) {
			throw new FileCreationException("Failed to create directories: " + path, e);
		}
	}

}
