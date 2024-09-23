package com.inesdatamap.mapperbackend.services;

import com.inesdatamap.mapperbackend.model.jpa.Execution;

/**
 * Service for operations related to the execution of mappings
 */
public interface ExecutionService {

	/**
	 * Save an execution
	 *
	 * @param execution
	 * 	the execution to save
	 *
	 * @return the saved execution
	 */
	Execution save(Execution execution);

}
