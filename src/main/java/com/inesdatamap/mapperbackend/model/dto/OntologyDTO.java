package com.inesdatamap.mapperbackend.model.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing an Ontology.
 */
@Getter
@Setter
public class OntologyDTO extends BaseEntityDTO {

	private String name;

	private byte[] content;

	private String title;

	private LocalDateTime uploadDate;

	private String url;

	private String versionName;

}
