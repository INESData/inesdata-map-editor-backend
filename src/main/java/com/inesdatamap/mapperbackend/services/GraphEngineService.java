package com.inesdatamap.mapperbackend.services;

import java.util.List;

import com.inesdatamap.mapperbackend.model.jpa.MappingField;

/**
 * Service to execute the graph engine
 */
public interface GraphEngineService {

	/**
	 * Run the graph engine
	 *
	 * @param mappingPath
	 * 	the path to the mapping file
	 * @param mappingId
	 * 	the id of the mapping
	 * @param mappingFields
	 * 	the mapping fields
	 *
	 * @return the result
	 */
	List<String> run(String mappingPath, Long mappingId, List<MappingField> mappingFields);

}
