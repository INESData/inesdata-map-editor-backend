package com.inesdatamap.mapperbackend.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing a ObjectMap.
 */
@Getter
@Setter
public class ObjectMapDTO {

	private String key;
	private String literalValue;
	private List<ObjectMapDTO> objectValue;

}
