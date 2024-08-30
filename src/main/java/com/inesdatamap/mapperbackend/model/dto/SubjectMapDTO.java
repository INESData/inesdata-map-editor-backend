package com.inesdatamap.mapperbackend.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing a SubjectMap.
 */
@Getter
@Setter
public class SubjectMapDTO extends BaseEntityDTO {

	private String template;
	private String className;

}
