package com.inesdatamap.mapperbackend.model.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing a SearchOntologyDTO.
 */
@Getter
@Setter
public class SearchOntologyDTO {

	private String name;

	private String title;

	private LocalDateTime uploadDate;

	private String url;

	private String versionName;

}
