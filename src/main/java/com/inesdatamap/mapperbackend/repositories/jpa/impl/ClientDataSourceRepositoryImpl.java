package com.inesdatamap.mapperbackend.repositories.jpa.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.inesdatamap.mapperbackend.exceptions.ClientDataSourceException;
import com.inesdatamap.mapperbackend.repositories.jpa.ClientDataSourceRepository;

/**
 * Repository for client data source operations.
 */
public class ClientDataSourceRepositoryImpl extends JdbcDaoSupport implements ClientDataSourceRepository {

	/**
	 * Constructor.
	 *
	 * @param dataSource
	 * 	the configured data source
	 */
	public ClientDataSourceRepositoryImpl(DataSource dataSource) {
		setDataSource(dataSource);
	}

	@Override
	public List<String> getTableNames() {

		ArrayList<String> tableNames = new ArrayList<>();

		JdbcTemplate template = getJdbcTemplate();
		DataSource dataSource = template != null ? template.getDataSource() : null;

		if (template != null && dataSource != null) {

			try (Connection connection = dataSource.getConnection()) {
				DatabaseMetaData metaData = connection.getMetaData();
				ResultSet tables = metaData.getTables(null, null, "%", new String[] { "TABLE" });
				while (tables.next()) {
					String tableName = tables.getString("TABLE_NAME");
					tableNames.add(tableName);
				}
			} catch (SQLException e) {
				throw new ClientDataSourceException(e.getMessage(), e);
			}
		}
		return tableNames;
	}
}
