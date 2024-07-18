package com.inesdatamap.mapperbackend.repositories.jpa;

import java.util.List;

public interface ClientDataSourceRepository {

	List<String> getTableNames();

}
