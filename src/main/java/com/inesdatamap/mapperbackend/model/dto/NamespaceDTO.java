package com.inesdatamap.mapperbackend.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing a Namespace.
 */
@Getter
@Setter
public class NamespaceDTO {

	private String prefix;
	private String iri;

}
