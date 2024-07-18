package com.inesdatamap.mapperbackend.services.impl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inesdatamap.mapperbackend.model.routing.ClientDatabaseContextHolder;
import com.inesdatamap.mapperbackend.repositories.jpa.ClientDataSourceRepository;
import com.inesdatamap.mapperbackend.repositories.jpa.impl.ClientDataSourceRepositoryImpl;
import com.inesdatamap.mapperbackend.services.ClientDataSourceService;

@Service
@Transactional
public class ClientDataSourceServiceImpl implements ClientDataSourceService {

	@Override
	public List<String> getTableNames(Long dataSourceId, DataSource dataSource) {

		ClientDataSourceRepository clientDataSourceRepository = new ClientDataSourceRepositoryImpl(dataSource);

		ClientDatabaseContextHolder.set(dataSourceId);
		List<String> result = clientDataSourceRepository.getTableNames();
		ClientDatabaseContextHolder.clear();

		return result;

	}
}
