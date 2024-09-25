package com.inesdatamap.mapperbackend.services.impl;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.inesdatamap.mapperbackend.exceptions.FileCreationException;
import com.inesdatamap.mapperbackend.model.dto.DataSourceDTO;
import com.inesdatamap.mapperbackend.model.dto.FileSourceDTO;
import com.inesdatamap.mapperbackend.model.enums.DataFileTypeEnum;
import com.inesdatamap.mapperbackend.model.jpa.FileSource;
import com.inesdatamap.mapperbackend.model.mappers.FileSourceMapper;
import com.inesdatamap.mapperbackend.properties.AppProperties;
import com.inesdatamap.mapperbackend.repositories.jpa.FileSourceRepository;
import com.inesdatamap.mapperbackend.services.FileSourceService;
import com.inesdatamap.mapperbackend.utils.Constants;
import com.inesdatamap.mapperbackend.utils.FileUtils;

import jakarta.persistence.EntityNotFoundException;

/**
 * File source service implementation
 *
 * @see FileSourceService
 */
@Service
@Transactional(rollbackFor = FileCreationException.class)
public class FileSourceServiceImpl implements FileSourceService {

	@Autowired
	private FileSourceMapper fileSourceMapper;

	@Autowired
	private FileSourceRepository fileSourceRepository;

	@Autowired
	private AppProperties appProperties;

	/**
	 * Saves a data source
	 *
	 * @param fileSourceDTO
	 * 	the FileSourceDTO
	 * @param file
	 * 	file content to save
	 *
	 * @return the saved data source
	 */
	@Override
	public DataSourceDTO createFileSource(FileSourceDTO fileSourceDTO, MultipartFile file) {

		if (fileSourceDTO == null) {
			throw new IllegalArgumentException("The file base source has no data");
		}

		// DTO to entity
		FileSource fileSource = this.fileSourceMapper.dtoToEntity(fileSourceDTO);

		// Save new entity
		FileSource savedFileSource = this.fileSourceRepository.save(fileSource);

		if (file != null && !file.isEmpty()) {

			// Validate the file content
			FileUtils.validateFile(file);

			// Read file headers
			String headers = FileUtils.processFileHeaders(file);

			// Build file path
			String filePath = String.join(File.separator, this.appProperties.getDataProcessingPath(), Constants.DATA_INPUT_FOLDER_NAME,
				savedFileSource.getId().toString());

			String fileName = UUID.randomUUID() + FilenameUtils.getName(file.getOriginalFilename());

			// Set values in FileSource
			savedFileSource.setFields(headers);
			savedFileSource.setFileName(fileName);
			savedFileSource.setFilePath(filePath);

			// Save file
			FileUtils.saveFile(file, fileName, filePath);

			savedFileSource = this.fileSourceRepository.save(savedFileSource);
		}

		return this.fileSourceMapper.entityToDto(savedFileSource);
	}

	/**
	 * Updates an existing file source
	 *
	 * @param id
	 * 	The identifier of the file source to be updated
	 * @param fileSourceDTO
	 * 	The FileSourceDTO
	 *
	 * @return the updated file source.
	 */
	@Override
	public DataSourceDTO updateFileSource(Long id, FileSourceDTO fileSourceDTO) {

		if (fileSourceDTO == null) {
			throw new IllegalArgumentException("The file source has no data");
		}

		// Get DB entity
		FileSource fileSourceDB = this.getEntity(id);

		// Update existing entity
		this.fileSourceMapper.merge(fileSourceDTO, fileSourceDB);

		return this.fileSourceMapper.entityToDto(fileSourceDB);
	}

	/**
	 * Retrieves a file source entity by its ID.
	 *
	 * @param id
	 * 	the ID of the file source to retrieve
	 *
	 * @return the file source entity corresponding to the given ID
	 */
	@Override
	public FileSource getEntity(Long id) {
		return this.fileSourceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id));
	}

	/**
	 * Retrieves a FileSourceDTO by its identifier.
	 *
	 * @param id
	 * 	the unique identifier of the file source entity
	 *
	 * @return the file source dto corresponding to the given ID
	 */
	@Override
	public FileSourceDTO getFileSourceById(Long id) {
		return this.fileSourceMapper.entityToDto(this.getEntity(id));

	}

	/**
	 * Retrieves a list of FileSourceDTO objects filtered by the specified file type.
	 *
	 * @param fileType
	 * 	The type of the file sources to retrieve
	 *
	 * @return A list of FileSourceDTO objects representing the file sources of the specified type.
	 */
	@Override
	public List<FileSourceDTO> getFileSourceByType(String fileType) {

		DataFileTypeEnum typeEnum = DataFileTypeEnum.valueOf(fileType);
		List<FileSource> fileSources = this.fileSourceRepository.findByFileTypeOrderByNameAsc(typeEnum);

		return this.fileSourceMapper.fileSourcesToFileSourceDTOs(fileSources);
	}

	/**
	 * Retrieves the fields of a file source as a list of strings, based on the given source ID.
	 *
	 * @param id
	 * 	The ID of the file source whose fields are to be retrieved.
	 *
	 * @return A list of field names extracted from the fields property of the FileSource entity
	 */
	@Override
	public List<String> getFileFields(Long id) {

		FileSource fileSource = this.getEntity(id);

		String fields = fileSource.getFields();
		if (fields != null && !fields.isEmpty()) {
			return Arrays.asList(fields.split(Constants.FIELD_DELIMITER_REGEX));
		} else {
			return Arrays.asList();
		}

	}

}
