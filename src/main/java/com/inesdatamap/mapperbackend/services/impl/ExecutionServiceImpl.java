package com.inesdatamap.mapperbackend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inesdatamap.mapperbackend.model.jpa.Execution;
import com.inesdatamap.mapperbackend.repositories.jpa.ExecutionRepository;
import com.inesdatamap.mapperbackend.services.ExecutionService;

/**
 * Execution service implementation
 */
@Service
public class ExecutionServiceImpl implements ExecutionService {

	@Autowired
	private ExecutionRepository repository;

	@Override
	public Execution save(Execution execution) {
		return repository.save(execution);
	}
}
