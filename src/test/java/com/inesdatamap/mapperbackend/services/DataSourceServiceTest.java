package com.inesdatamap.mapperbackend.services;

import java.util.Optional;

import javax.sql.DataSource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.inesdatamap.mapperbackend.model.enums.DataBaseTypeEnum;
import com.inesdatamap.mapperbackend.model.jpa.DataBaseSource;
import com.inesdatamap.mapperbackend.repositories.jpa.DataSourceRepository;
import com.inesdatamap.mapperbackend.services.impl.DataSourceServiceImpl;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DataSourceServiceImpl.class)
class DataSourceServiceTest {

	@MockBean
	private DataSourceRepository<DataBaseSource> dataBaseSourceRepository;

	@Autowired
	@InjectMocks
	private DataSourceServiceImpl dataSourceService;

	@Test
	void testFindById() {

		// mock
		DataBaseSource dbs = new DataBaseSource();
		dbs.setId(1L);

		Mockito.when(this.dataBaseSourceRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(dbs));

		// test
		DataBaseSource result = dataSourceService.findById(1L);

		// asserts
		Assertions.assertEquals(dbs.getId(), result.getId());
	}

	@Test
	void testFindByIdThrowsNotEntityFoundException() {

		// mock
		DataBaseSource dbs = new DataBaseSource();
		dbs.setId(1L);

		Mockito.when(this.dataBaseSourceRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

		// asserts
		Assertions.assertThrows(EntityNotFoundException.class, () -> dataSourceService.findById(1L));
	}

	@Test
	void testGetClientDataSource() {

		// mock
		DataBaseSource dbs = new DataBaseSource();
		dbs.setId(1L);
		dbs.setUser("");
		dbs.setPassword("");
		dbs.setConnectionString("jdbc:mysql://localhost:3306/test");
		dbs.setDatabaseType(DataBaseTypeEnum.MYSQL);

		Mockito.when(this.dataBaseSourceRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(dbs));

		// test
		DataSource ds = dataSourceService.getClientDataSource(1L);

		// asserts
		Assertions.assertNotNull(ds);

	}

}
