package com.inesdatamap.mapperbackend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.inesdatamap.mapperbackend.model.dto.ExecutionDTO;
import com.inesdatamap.mapperbackend.model.jpa.Execution;
import com.inesdatamap.mapperbackend.model.mappers.ExecutionMapper;
import com.inesdatamap.mapperbackend.repositories.jpa.ExecutionRepository;
import com.inesdatamap.mapperbackend.services.ExecutionService;

/**
 * Execution service implementation
 */
@Service
public class ExecutionServiceImpl implements ExecutionService {

	@Autowired
	private ExecutionRepository repository;

	@Autowired
	private ExecutionMapper executionMapper;

	@Override
	public Execution save(Execution execution) {
		return repository.save(execution);
	}

	@Override
	public Page<ExecutionDTO> listExecutions(Long mappingId, Pageable pageable) {

		Page<Execution> executions = repository.findByMappingId(mappingId, pageable);

		return executions.map(executionMapper::entityToDto);
	}

}
