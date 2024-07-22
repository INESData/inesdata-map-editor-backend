package com.inesdatamap.mapperbackend.services;

import javax.sql.DataSource;

import com.inesdatamap.mapperbackend.model.jpa.DataBaseSource;

/**
 * Data source service
 */
public interface DataSourceService {

	/**
	 * Find a data source by id
	 *
	 * @param dataSourceId
	 * 	the data source id
	 *
	 * @return the data source
	 */
	DataBaseSource findById(Long dataSourceId);

	/**
	 * Get the client data source
	 *
	 * @param dataSourceId
	 * 	the data source id
	 *
	 * @return the configured client data source
	 */
	DataSource getClientDataSource(Long dataSourceId);

}
