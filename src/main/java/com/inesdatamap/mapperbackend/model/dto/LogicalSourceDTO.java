package com.inesdatamap.mapperbackend.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing a LogicalSource.
 */
@Getter
@Setter
public class LogicalSourceDTO extends BaseEntityDTO {

	private String source;
	private String referenceFormulation;
	private String iterator;
	private String query;

}
