package com.inesdatamap.mapperbackend.services;

import java.util.List;

/**
 * Service to execute the graph engine
 */
public interface GraphEngineService {

	/**
	 * Run the graph engine
	 *
	 * @param rml
	 * 	the RML content
	 *
	 * @return the result
	 */
	List<String> run(String rml);

}
