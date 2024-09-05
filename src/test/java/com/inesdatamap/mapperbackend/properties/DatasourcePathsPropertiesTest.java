package com.inesdatamap.mapperbackend.properties;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test for the {@link DatasourcePathsProperties} class
 */
@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = DatasourcePathsProperties.class)
@TestPropertySource(properties = { "spring.config.location=classpath:application.yaml" })
@ContextConfiguration(initializers = ConfigDataApplicationContextInitializer.class)
class DatasourcePathsPropertiesTest {

	@Autowired
	private DatasourcePathsProperties properties;

	@Test
	void getRmlTest() {
		assertEquals("/test/tmp", properties.getRml());
	}

	@Test
	void getDataInputTest() {
		assertEquals("/test/input", properties.getDataInput());
	}

	@Test
	void getDataOutputTest() {
		assertEquals("/test/output", properties.getDataOutput());
	}

}
