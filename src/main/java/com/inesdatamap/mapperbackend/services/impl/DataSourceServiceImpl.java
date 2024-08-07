package com.inesdatamap.mapperbackend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inesdatamap.mapperbackend.model.dto.DataSourceDTO;
import com.inesdatamap.mapperbackend.model.jpa.DataBaseSource;
import com.inesdatamap.mapperbackend.model.jpa.DataSource;
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

	@Override
	public DataBaseSource findById(Long dataSourceId) {
		return this.dataBaseSourceRepository.findById(dataSourceId).orElseThrow(() -> new EntityNotFoundException(dataSourceId.toString()));
	}

	@Override
	public javax.sql.DataSource getClientDataSource(Long dataSourceId) {

		DataBaseSource ds = this.findById(dataSourceId);
		ClientDataSourceRouter router = new ClientDataSourceRouter();

		return router.getDatasource(dataSourceId, ds.getConnectionString(), ds.getDatabaseType(), ds.getUser(), ds.getPassword());
	}

	@Override
	public Page<DataSourceDTO> listDataSources(Pageable pageable) {
		Page<DataSource> dataSourcesPage = this.dataSourceRepository.findAll(pageable);

		return dataSourcesPage.map(this.dataSourceMapper::dataSourceToDTO);

	}

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
				.orElseThrow(() -> new EntityNotFoundException("Entity not found with id" + id.toString()));
	}

}
