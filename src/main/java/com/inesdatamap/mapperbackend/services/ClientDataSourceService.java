package com.inesdatamap.mapperbackend.services;

import java.util.List;

import javax.sql.DataSource;

public interface ClientDataSourceService {

	List<String> getTableNames(Long dataSourceId, DataSource dataSource);

}
