package com.inesdatamap.mapperbackend.services;

import org.springframework.web.multipart.MultipartFile;

import com.inesdatamap.mapperbackend.model.dto.DataSourceDTO;
import com.inesdatamap.mapperbackend.model.dto.FileSourceDTO;
import com.inesdatamap.mapperbackend.model.jpa.FileSource;

/**
 * File source service
 */
public interface FileSourceService {

	/**
	 * Saves a file source
	 *
	 * @param fileSourceDTO
	 *            the file source
	 * @param file
	 *            the file to save
	 * @return the saved file source
	 */
	DataSourceDTO createFileSource(FileSourceDTO fileSourceDTO, MultipartFile file);

	/**
	 * Saves a file source
	 *
	 * @param id
	 *            file source identifier
	 *
	 * @param fileSourceDTO
	 *            the file source
	 * @return the saved file source
	 */
	DataSourceDTO updateFileSource(Long id, FileSourceDTO fileSourceDTO);

	/**
	 * Retrieves a file source entity by its ID.
	 *
	 * @param id
	 *            the ID of the file source to retrieve
	 * @return the file source entity corresponding to the given ID
	 */
	FileSource getEntity(Long id);

}
