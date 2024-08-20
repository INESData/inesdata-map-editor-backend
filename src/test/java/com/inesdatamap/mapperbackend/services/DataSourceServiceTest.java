package com.inesdatamap.mapperbackend.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.inesdatamap.mapperbackend.model.dto.DataSourceDTO;
import com.inesdatamap.mapperbackend.model.enums.DataBaseTypeEnum;
import com.inesdatamap.mapperbackend.model.enums.DataSourceTypeEnum;
import com.inesdatamap.mapperbackend.model.jpa.DataBaseSource;
import com.inesdatamap.mapperbackend.model.jpa.DataSource;
import com.inesdatamap.mapperbackend.model.jpa.FileSource;
import com.inesdatamap.mapperbackend.model.mappers.DataSourceMapper;
import com.inesdatamap.mapperbackend.repositories.jpa.DataSourceRepository;
import com.inesdatamap.mapperbackend.services.impl.DataSourceServiceImpl;
import com.inesdatamap.mapperbackend.utils.Constants;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class DataSourceServiceTest {

	@Mock
	private DataSourceRepository<DataBaseSource> dataBaseSourceRepository;

	@Mock
	private DataSourceRepository<DataSource> dataSourceRepository;

	@Mock
	private DataSourceMapper dataSourceMapper;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private DataSourceServiceImpl dataSourceService;

	@Test
	void testFindById() {
		// mock
		DataBaseSource dbs = new DataBaseSource();
		dbs.setId(1L);

		Mockito.when(this.dataBaseSourceRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(dbs));

		// test
		DataBaseSource result = this.dataSourceService.findById(1L);

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
		Assertions.assertThrows(EntityNotFoundException.class, () -> this.dataSourceService.findById(1L));
	}

	@Test
	void testGetClientDataSource() {

		// mock
		DataBaseSource dbs = new DataBaseSource();
		dbs.setId(1L);
		dbs.setUserName("");
		dbs.setPassword("");
		dbs.setConnectionString("jdbc:mysql://localhost:3306/test");
		dbs.setDatabaseType(DataBaseTypeEnum.MYSQL);

		Mockito.when(this.dataBaseSourceRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(dbs));

		// test
		javax.sql.DataSource ds = this.dataSourceService.getClientDataSource(1L);

		// asserts
		Assertions.assertNotNull(ds);

	}

	@Test
	void testListDataSources() {

		// Mock data
		DataSource dataSource = new DataSource();
		DataSourceDTO dataSourceDTO = new DataSourceDTO();
		Page<DataSource> page = new PageImpl<>(List.of(dataSource));
		when(this.dataSourceRepository.findAll(any(Pageable.class))).thenReturn(page);
		when(this.dataSourceMapper.dataSourceToDTO(any(DataSource.class))).thenReturn(dataSourceDTO);

		// Execute the method
		Page<DataSourceDTO> result = this.dataSourceService.listDataSources(Pageable.unpaged());

		// Verify
		assertThat(result).isNotNull();
		assertThat(result.getContent()).containsExactly(dataSourceDTO);
	}

	@Test
	void testDeleteDataSource() {

		// Mock data
		Long id = 1L;
		DataSource dataSource = new DataSource();
		when(this.dataSourceRepository.findById(id)).thenReturn(Optional.of(dataSource));

		// Execute the method
		this.dataSourceService.deleteDataSource(id);

		// Verify
		verify(this.dataSourceRepository, times(1)).deleteById(id);
	}

	@Test
	void testCreateDataSourceWithFile() throws Exception {

		// Mock data
		DataSourceDTO dataSourceDto = new DataSourceDTO();
		dataSourceDto.setType(DataSourceTypeEnum.FILE);
		MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", "header1,header2".getBytes());
		FileSource fileSource = new FileSource();
		fileSource.setFields("header1,header2");
		fileSource.setFileName("test.csv");

		// Mock behavior
		when(this.dataSourceMapper.dataSourceDtoToFileSource(dataSourceDto)).thenReturn(fileSource);
		when(this.dataSourceRepository.save(any(FileSource.class))).thenReturn(fileSource);
		when(this.dataSourceMapper.dataSourceToDTO(any(FileSource.class))).thenReturn(dataSourceDto);

		// Execute the method
		DataSourceDTO result = this.dataSourceService.createDataSource(dataSourceDto, file);

		// Verify
		assertThat(result).isNotNull();
		verify(this.dataSourceRepository, times(1)).save(any(FileSource.class));
		verify(this.dataSourceMapper, times(1)).dataSourceToDTO(any(FileSource.class));
	}

	@Test
	void testCreateDataSourceWithDatabase() {

		// Mock data
		DataSourceDTO dataSourceDto = new DataSourceDTO();
		dataSourceDto.setType(DataSourceTypeEnum.DATABASE);
		dataSourceDto.setPassword(Constants.TEST_PASSWORD);
		DataBaseSource dataBaseSource = new DataBaseSource();
		String encodedPassword = Constants.ENCODED_PASSWORD;

		// Mock behavior
		when(this.dataSourceMapper.dataSourceDtoToDataBase(dataSourceDto)).thenReturn(dataBaseSource);
		when(this.passwordEncoder.encode(Constants.TEST_PASSWORD)).thenReturn(encodedPassword);
		when(this.dataSourceRepository.save(any(DataBaseSource.class))).thenReturn(dataBaseSource);
		when(this.dataSourceMapper.dataSourceToDTO(any(DataBaseSource.class))).thenReturn(dataSourceDto);

		// Execute the method
		DataSourceDTO result = this.dataSourceService.createDataSource(dataSourceDto, null);

		// Verify
		assertThat(result).isNotNull();
		verify(this.dataSourceRepository, times(1)).save(any(DataBaseSource.class));
		verify(this.dataSourceMapper, times(1)).dataSourceToDTO(any(DataBaseSource.class));
		verify(this.passwordEncoder, times(1)).encode(Constants.TEST_PASSWORD);
	}

	@Test
	void testUpdateDataSourceWithFile() throws IOException {
		// Mock data
		Long id = 1L;
		DataSourceDTO dataSourceDto = new DataSourceDTO();
		dataSourceDto.setType(DataSourceTypeEnum.FILE);
		FileSource existingFileSource = new FileSource();
		FileSource updatedFileSource = new FileSource();
		MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", "header1,header2".getBytes());

		// Mock behavior
		when(this.dataSourceRepository.findById(id)).thenReturn(Optional.of(existingFileSource));
		when(this.dataSourceMapper.dataSourceDtoToFileSource(dataSourceDto)).thenReturn(updatedFileSource);
		when(this.dataSourceRepository.saveAndFlush(any(FileSource.class))).thenReturn(updatedFileSource);
		when(this.dataSourceMapper.mergeFileSource(any(FileSource.class), any(FileSource.class))).thenReturn(updatedFileSource);
		when(this.dataSourceMapper.entityToDto(any(FileSource.class))).thenReturn(dataSourceDto);

		// Execute the method
		DataSourceDTO result = this.dataSourceService.updateDataSource(id, dataSourceDto, file);

		// Verify
		assertThat(result).isNotNull();
		verify(this.dataSourceRepository, times(1)).saveAndFlush(any(FileSource.class));
		verify(this.dataSourceMapper, times(1)).mergeFileSource(any(FileSource.class), any(FileSource.class));
		verify(this.dataSourceMapper, times(1)).entityToDto(any(FileSource.class));
	}

	@Test
	void testUpdateDataSourceWithDatabase() {

		// Mock data
		Long id = 1L;
		DataSourceDTO dataSourceDto = new DataSourceDTO();
		dataSourceDto.setType(DataSourceTypeEnum.DATABASE);
		dataSourceDto.setPassword(Constants.TEST_PASSWORD);

		DataBaseSource existingDataBaseSource = new DataBaseSource();
		existingDataBaseSource.setPassword(Constants.ENCODED_PASSWORD);

		DataBaseSource updatedDataBaseSource = new DataBaseSource();

		// Mock behavior
		when(this.dataSourceRepository.findById(id)).thenReturn(Optional.of(existingDataBaseSource));
		when(this.dataSourceMapper.dataSourceDtoToDataBase(dataSourceDto)).thenReturn(updatedDataBaseSource);
		when(this.dataSourceRepository.saveAndFlush(any(DataBaseSource.class))).thenReturn(updatedDataBaseSource);
		when(this.passwordEncoder.matches(Constants.TEST_PASSWORD, Constants.ENCODED_PASSWORD)).thenReturn(false);
		when(this.passwordEncoder.encode(Constants.TEST_PASSWORD)).thenReturn("newEncodedPassword");
		when(this.dataSourceMapper.mergeDataBaseSource(any(DataBaseSource.class), any(DataBaseSource.class)))
				.thenReturn(updatedDataBaseSource);
		when(this.dataSourceMapper.entityToDto(any(DataBaseSource.class))).thenReturn(dataSourceDto);

		// Execute the method
		DataSourceDTO result = this.dataSourceService.updateDataSource(id, dataSourceDto, null);

		// Verify
		assertThat(result).isNotNull();
		verify(this.dataSourceRepository, times(1)).saveAndFlush(any(DataBaseSource.class));
		verify(this.dataSourceMapper, times(1)).mergeDataBaseSource(any(DataBaseSource.class), any(DataBaseSource.class));
		verify(this.dataSourceMapper, times(1)).entityToDto(any(DataBaseSource.class));
	}

	@Test
	void testGetEntity() {
		// Prepare mock data
		Long id = 1L;
		DataSource dataSource = new DataSource();
		when(this.dataSourceRepository.findById(id)).thenReturn(Optional.of(dataSource));

		// Execute the method
		DataSource result = this.dataSourceService.getEntity(id);

		// Verify results
		assertThat(result).isEqualTo(dataSource);
	}

}
