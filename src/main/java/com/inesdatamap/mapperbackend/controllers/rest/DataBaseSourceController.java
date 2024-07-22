package com.inesdatamap.mapperbackend.controllers.rest;

import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inesdatamap.mapperbackend.services.ClientDataSourceService;
import com.inesdatamap.mapperbackend.services.DataSourceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Database source controller
 */
@RestController()
@RequestMapping("/database-sources")
@Tag(name = "DataBaseSource", description = "Manage database sources")
public class DataBaseSourceController {

	private final ClientDataSourceService clientDataSourceService;
	private final DataSourceService dataSourceService;

	/**
	 * Constructor
	 *
	 * @param clientDataSourceService
	 * 	the client data source service
	 * @param dataSourceService
	 * 	the data source service
	 */
	public DataBaseSourceController(ClientDataSourceService clientDataSourceService, DataSourceService dataSourceService) {
		this.clientDataSourceService = clientDataSourceService;
		this.dataSourceService = dataSourceService;
	}

	/**
	 * Get table names for a database source
	 *
	 * @param id
	 * 	the data source ID
	 *
	 * @return the list of table names
	 */
	@GetMapping("/{id}/table-names")
	@Operation(summary = "Get table names for a database source", description = "Retrieves a list of table names for the specified data source ID.")
	public ResponseEntity<List<String>> getTableNames(@PathVariable Long id) {

		DataSource dbDataSource = this.dataSourceService.getClientDataSource(id);

		return ResponseEntity.ok(clientDataSourceService.getTableNames(id, dbDataSource));
	}

	/**
	 * Get column names for a database table
	 *
	 * @param id
	 * 	the data source ID
	 * @param table
	 * 	the table name
	 *
	 * @return the list of table names
	 */
	@GetMapping("/{id}/table-column-names")
	@Operation(summary = "Get column names for a database table", description = "Retrieves a list of column names for the specified table name and a data source ID.")
	public ResponseEntity<List<String>> getColumnNames(@PathVariable Long id, @RequestParam(name = "table") String table) {

		if (StringUtils.isEmpty(table)) {
			throw new IllegalArgumentException("Table name is required");
		}

		DataSource dbDataSource = this.dataSourceService.getClientDataSource(id);

		return ResponseEntity.ok(clientDataSourceService.getColumnNames(id, table, dbDataSource));
	}

}
