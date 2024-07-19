package com.inesdatamap.mapperbackend.repositories.jpa;

import java.util.List;

/**
 * Repository for client data source operations.
 */
public interface ClientDataSourceRepository {

	/**
	 * Get table names from a database data source.
	 *
	 * @return the list of table names
	 */
	List<String> getTableNames();

}
