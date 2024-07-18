package com.inesdatamap.mapperbackend.model.routing;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Router for the client data source
 */
public class ClientDataSourceRouter extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		return ClientDatabaseContextHolder.get();
	}

	public DataSource getDatasource(Long dataSourceId, String url, String driver, String username, String password) {
		Map<Object, Object> dataSourceMap = new HashMap<>();

		DataSource ds = DataSourceBuilder.create().driverClassName(driver).url(url).username(username).password(password).build();

		dataSourceMap.put(dataSourceId, ds);
		this.setTargetDataSources(dataSourceMap);
		this.setDefaultTargetDataSource(ds);

		return ds;
	}

}
