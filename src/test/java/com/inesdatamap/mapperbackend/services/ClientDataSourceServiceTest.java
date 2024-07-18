package com.inesdatamap.mapperbackend.services;

import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.inesdatamap.mapperbackend.model.routing.ClientDataSourceRouter;
import com.inesdatamap.mapperbackend.services.impl.ClientDataSourceServiceImpl;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ClientDataSourceServiceImpl.class)
class ClientDataSourceServiceTest {

	@Autowired
	private ClientDataSourceService clientDataSourceService;

	@Test
	void testGetTableNames() {

		long dataSourceAId = 1L;
		String urlDatasourceA = "jdbc:h2:mem:testdba";
		String urlDatasourceB = "jdbc:h2:mem:testdbb";
		String driver = "org.h2.Driver";
		String username = "";
		String password = "";

		ClientDataSourceRouter router = new ClientDataSourceRouter();

		DataSource dsA = router.getDatasource(dataSourceAId, urlDatasourceA, driver, username, password);
		List<String> dsATables = clientDataSourceService.getTableNames(dataSourceAId, dsA);

		DataSource dsB = router.getDatasource(dataSourceAId, urlDatasourceB, driver, username, password);
		List<String> dsBTables = clientDataSourceService.getTableNames(dataSourceAId, dsB);

		dsATables.forEach(System.out::println);

		Assertions.assertEquals(dsATables.size(), dsBTables.size());
		Assertions.assertFalse(dsATables.isEmpty());

	}

}
