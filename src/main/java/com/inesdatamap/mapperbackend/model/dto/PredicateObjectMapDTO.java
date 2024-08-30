package com.inesdatamap.mapperbackend.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing a PredicateObjectMap.
 */
@Getter
@Setter
public class PredicateObjectMapDTO {

	private String predicate;
	private List<ObjectMapDTO> objectMap;

}
