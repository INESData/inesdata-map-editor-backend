package com.inesdatamap.mapperbackend.services;

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

}
