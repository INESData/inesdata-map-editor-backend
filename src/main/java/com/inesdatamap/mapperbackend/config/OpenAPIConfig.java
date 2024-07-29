package com.inesdatamap.mapperbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.inesdatamap.mapperbackend.properties.RestApiInfoProperties;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * OpenAPI configuration
 */
@Configuration
public class OpenAPIConfig {

	/**
	 * Gets the OpenAPI info from properties
	 *
	 * @param restApiInfoProperties
	 * 	the REST API info properties
	 *
	 * @return the OpenAPI info
	 */
	@Bean
	public OpenAPI getOpenApiInfo(RestApiInfoProperties restApiInfoProperties) {
		return new OpenAPI().info(new Info().title(restApiInfoProperties.getName()).version(restApiInfoProperties.getVersion())
			.description(restApiInfoProperties.getDescription()));
	}

}
