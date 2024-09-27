package com.inesdatamap.mapperbackend.model.dto;

import com.inesdatamap.mapperbackend.model.enums.DataFileTypeEnum;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing a {@link com.inesdatamap.mapperbackend.model.jpa.LogicalSource}.
 */
@Getter
@Setter
public class LogicalSourceDTO extends BaseEntityDTO {

	@NotNull
	@Size(min = 1, max = 255)
	private String source;

	@NotNull
	private DataFileTypeEnum fileType;

	@Size(min = 1, max = 255)
	private String query;

}