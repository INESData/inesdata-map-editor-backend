package com.inesdatamap.mapperbackend.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inesdatamap.mapperbackend.model.jpa.DataBaseSource;
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

	private DataSourceRepository<DataBaseSource> dataBaseSourceRepository;

	/**
	 * Constructor
	 *
	 * @param dataBaseSourceRepository
	 * 	the database source repository
	 */
	public DataSourceServiceImpl(DataSourceRepository<DataBaseSource> dataBaseSourceRepository) {
		this.dataBaseSourceRepository = dataBaseSourceRepository;
	}

	@Override
	public DataBaseSource findById(Long dataSourceId) {
		return dataBaseSourceRepository.findById(dataSourceId).orElseThrow(() -> new EntityNotFoundException(dataSourceId.toString()));
	}

}
