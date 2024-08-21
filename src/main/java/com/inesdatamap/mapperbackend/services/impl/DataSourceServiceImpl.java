package com.inesdatamap.mapperbackend.services.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.inesdatamap.mapperbackend.model.dto.DataSourceDTO;
import com.inesdatamap.mapperbackend.model.enums.DataSourceTypeEnum;
import com.inesdatamap.mapperbackend.model.jpa.DataBaseSource;
import com.inesdatamap.mapperbackend.model.jpa.DataSource;
import com.inesdatamap.mapperbackend.model.jpa.FileSource;
import com.inesdatamap.mapperbackend.model.mappers.DataSourceMapper;
import com.inesdatamap.mapperbackend.model.routing.ClientDataSourceRouter;
import com.inesdatamap.mapperbackend.repositories.jpa.DataSourceRepository;
import com.inesdatamap.mapperbackend.services.DataSourceService;

import jakarta.persistence.EntityNotFoundException;

/**
 * Data source service implementation
 *
 * @see DataSourceService
 */
@Service
@Transactional
public class DataSourceServiceImpl implements DataSourceService {

	@Autowired
	private DataSourceRepository<DataBaseSource> dataBaseSourceRepository;

	@Autowired
	private DataSourceRepository<DataSource> dataSourceRepository;

	@Autowired
	private DataSourceMapper dataSourceMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public DataBaseSource findById(Long dataSourceId) {
		return this.dataBaseSourceRepository.findById(dataSourceId).orElseThrow(() -> new EntityNotFoundException(dataSourceId.toString()));
	}

	@Override
	public javax.sql.DataSource getClientDataSource(Long dataSourceId) {

		DataBaseSource ds = this.findById(dataSourceId);
		ClientDataSourceRouter router = new ClientDataSourceRouter();

		return router.getDatasource(dataSourceId, ds.getConnectionString(), ds.getDatabaseType(), ds.getUserName(), ds.getPassword());
	}

	/**
	 * Retrieves a list of all data sources and maps them to their corresponding DTOs.
	 *
	 * @return List of data sources
	 */
	@Override
	public Page<DataSourceDTO> listDataSources(Pageable pageable) {
		Page<DataSource> dataSourcesPage = this.dataSourceRepository.findAll(pageable);

		return dataSourcesPage.map(this.dataSourceMapper::dataSourceToDTO);

	}

	/**
	 * Deletes a data source by its id.
	 *
	 * @param id
	 *            the ID of the data source to delete
	 */
	@Override
	public void deleteDataSource(Long id) {

		// Get entity if exists
		this.getEntity(id);

		this.dataSourceRepository.deleteById(id);

	}

	/**
	 * Retrieves a data source entity by its ID.
	 *
	 * @param id
	 *            the ID of the data source to retrieve
	 * @return the data source entity corresponding to the given ID
	 */
	@Override
	public DataSource getEntity(Long id) {
		return this.dataSourceRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id.toString()));
	}

	/**
	 * Saves a data source
	 *
	 * @param dataSourceDTO
	 *            the DataSourceDTO
	 * @param file
	 *            file content to save
	 * @return the saved data source
	 */
	@Override
	public DataSourceDTO createDataSource(DataSourceDTO dataSourceDTO, MultipartFile file) {

		DataSource dataSource = new DataSource();

		if (dataSourceDTO.getType() == DataSourceTypeEnum.FILE) {
			// Map DTO to a FileSource entity
			FileSource fileSource = this.dataSourceMapper.dataSourceDtoToFileSource(dataSourceDTO);

			if (file != null && !file.isEmpty()) {
				try {
					// Read file content
					BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));

					// Read file first line with headers
					String headers = reader.readLine();

					// Set headers
					fileSource.setFields(headers);
					fileSource.setFileName(file.getOriginalFilename());

				} catch (IOException e) {
					throw new UncheckedIOException("Failed to store file headers", e);
				}
			}
			dataSource = fileSource;
		} else if (dataSourceDTO.getType() == DataSourceTypeEnum.DATABASE) {

			// Map DTO to a DataBaseSource entity
			dataSource = this.dataSourceMapper.dataSourceDtoToDataBase(dataSourceDTO);
		}

		// Save new entity
		DataSource savedDataSource = this.dataSourceRepository.save(dataSource);

		return this.dataSourceMapper.dataSourceToDTO(savedDataSource);
	}

	/**
	 * Updates a data source identified by its ID.
	 *
	 * @param id
	 *            the ID of the data source to update
	 * @param dataSourceDTO
	 *            the DataSourceDTO
	 * @param file
	 *            file content to update
	 * @return the updated data source
	 */
	@Override
	public DataSourceDTO updateDataSource(Long id, DataSourceDTO dataSourceDTO, MultipartFile file) {

		// Get DB entity
		DataSource dataSourceDB = this.getEntity(id);
		DataSource dataSourceUpdated = new DataSource();

		if (dataSourceDTO.getType() == DataSourceTypeEnum.FILE) {

			// New file source to save
			FileSource newFileSource = this.dataSourceMapper.dataSourceDtoToFileSource(dataSourceDTO);

			if (file != null && !file.isEmpty()) {
				try {
					// Read file content
					BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));

					// Read file first line with headers
					String headers = reader.readLine();

					// Set headers
					newFileSource.setFields(headers);
					newFileSource.setFileName(file.getOriginalFilename());

				} catch (IOException e) {
					throw new UncheckedIOException("Failed to update file headers", e);
				}
			}
			dataSourceUpdated = this.dataSourceMapper.mergeFileSource(newFileSource, (FileSource) dataSourceDB);

		} else if (dataSourceDTO.getType() == DataSourceTypeEnum.DATABASE) {

			// Map DTO to a DataBaseSource entity
			DataBaseSource newDataBaseSource = this.dataSourceMapper.dataSourceDtoToDataBase(dataSourceDTO);

			if (dataSourceDTO.getPassword() == null) {
				// Keep password if it has not changed
				newDataBaseSource.setPassword(((DataBaseSource) dataSourceDB).getPassword());
			}

			dataSourceUpdated = this.dataSourceMapper.mergeDataBaseSource(newDataBaseSource, (DataBaseSource) dataSourceDB);

		}
		dataSourceUpdated = this.dataSourceRepository.saveAndFlush(dataSourceUpdated);
		return this.dataSourceMapper.entityToDto(dataSourceUpdated);
	}

}
