package com.inesdatamap.mapperbackend.services;

import java.util.List;

/**
 * Service to execute the graph engine
 */
public interface GraphEngineService {

	/**
	 * Run the graph engine
	 *
	 * @return the result
	 *
	 * @throws InterruptedException
	 * 	the exception
	 */
	List<String> run() throws InterruptedException;

}
