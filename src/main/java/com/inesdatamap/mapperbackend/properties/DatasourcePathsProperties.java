package com.inesdatamap.mapperbackend.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * Configuration properties for datasource paths
 */
@Configuration
@ConfigurationProperties(prefix = "datasource-paths")
@Getter
@Setter
public class DatasourcePathsProperties {

	private String rml;
	private String dataInput;
	private String dataOutput;

}
