package com.inesdatamap.mapperbackend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.inesdatamap.mapperbackend.model.dto.DataSourceDTO;
import com.inesdatamap.mapperbackend.model.dto.FileSourceDTO;
import com.inesdatamap.mapperbackend.model.jpa.FileSource;
import com.inesdatamap.mapperbackend.model.mappers.FileSourceMapper;
import com.inesdatamap.mapperbackend.repositories.jpa.FileSourceRepository;
import com.inesdatamap.mapperbackend.services.FileSourceService;
import com.inesdatamap.mapperbackend.utils.FileUtils;

import jakarta.persistence.EntityNotFoundException;

/**
 * File source service implementation
 *
 * @see FileSourceService
 */
@Service
@Transactional
public class FileSourceServiceImpl implements FileSourceService {

	@Autowired
	private FileSourceMapper fileSourceMapper;

	@Autowired
	private FileSourceRepository fileSourceRepository;

	/**
	 * Saves a data source
	 *
	 * @param fileSourceDTO
	 *            the FileSourceDTO
	 * @param file
	 *            file content to save
	 * @return the saved data source
	 */
	@Override
	public DataSourceDTO createFileSource(FileSourceDTO fileSourceDTO, MultipartFile file) {

		if (fileSourceDTO == null) {
			throw new IllegalArgumentException("The file base source has no data");
		}

		// DTO to entity
		FileSource fileSource = this.fileSourceMapper.dtoToEntity(fileSourceDTO);

		if (file != null && !file.isEmpty()) {

			// Validate the file extension
			FileUtils.validateFileExtension(file.getContentType());

			// Read file headers
			FileUtils.processFileHeaders(file, fileSource);
		}

		// Save new entity
		FileSource savedFileSource = this.fileSourceRepository.save(fileSource);

		return this.fileSourceMapper.toDTO(savedFileSource);
	}

	/**
	 * Updates an existing file source
	 *
	 * @param id
	 *            The identifier of the file source to be updated
	 * @param fileSourceDTO
	 *            The FileSourceDTO
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

		return this.fileSourceMapper.toDTO(fileSourceDB);
	}

	/**
	 * Retrieves a file source entity by its ID.
	 *
	 * @param id
	 *            the ID of the file source to retrieve
	 * @return the file source entity corresponding to the given ID
	 */
	@Override
	public FileSource getEntity(Long id) {
		return this.fileSourceRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id.toString()));
	}

	/**
	 * Retrieves a FileSourceDTO by its identifier.
	 *
	 * @param id
	 *            the unique identifier of the file source entity
	 * @return the file source dto corresponding to the given ID
	 */
	@Override
	public FileSourceDTO getFileSourceById(Long id) {
		return this.fileSourceMapper.entityToDto(this.getEntity(id));

	}

}
