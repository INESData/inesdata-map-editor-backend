package com.inesdatamap.mapperbackend.model.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.inesdatamap.mapperbackend.model.dto.DataSourceDTO;
import com.inesdatamap.mapperbackend.model.jpa.DataBaseSource;
import com.inesdatamap.mapperbackend.model.jpa.DataSource;
import com.inesdatamap.mapperbackend.model.jpa.FileSource;

/**
 * Mapper interface for converting between DataSource and DataSourceDTO.
 *
 */
@Mapper(componentModel = "spring", uses = BaseEntityMapper.class)
public interface DataSourceMapper extends BaseEntityMapper<DataSourceDTO, DataSource> {

	/**
	 * Converts a DataSource entity to its corresponding DataSourceDTO.
	 *
	 * This method checks the type of the given entity and uses the appropriate
	 *
	 * conversion method for DataBaseSource or FileSource.
	 *
	 * @param entity
	 *            the DataSource entity to be converted.
	 * @return the corresponding DataSourceDTO for the given entity.
	 */
	default DataSourceDTO dataSourceToDTO(DataSource entity) {
		if (entity instanceof DataBaseSource database) {
			return this.dataBaseToDTO(database);
		} else if (entity instanceof FileSource fileSource) {
			return this.dataFileToDTO(fileSource);

		}
		return null;
	}

	/**
	 * Converts a DataBaseSource entity to its corresponding DataSourceDTO.
	 *
	 * @param dataBaseSource
	 *            the DataBaseSource entity to be converted.
	 * @return the corresponding DataSourceDTO.
	 */

	DataSourceDTO dataBaseToDTO(DataBaseSource dataBaseSource);

	/**
	 * Converts a FileSource entity to its corresponding DataSourceDTO.
	 *
	 * @param fileSource
	 *            the FileSource entity to be converted.
	 * @return the corresponding DataSourceDTO.
	 */
	DataSourceDTO dataFileToDTO(FileSource fileSource);

	/**
	 * Converts a DataSourceDTO object to its corresponding DataBaseSource entity.
	 *
	 * @param dataBaseSource
	 *            the DataSourceDTO object to be converted.
	 * @return the corresponding DataBaseSource entity.
	 */
	DataBaseSource dataSourceDtoToDataBase(DataSourceDTO dataBaseSource);

	/**
	 * Converts a DataSourceDTO object to its corresponding FileSource entity.
	 *
	 * @param dataBaseSource
	 *            the DataSourceDTO object to be converted.
	 * @return the corresponding FileSource entity.
	 */
	FileSource dataSourceDtoToFileSource(DataSourceDTO dataBaseSource);

	/**
	 * Merges the properties of a source DataBaseSource instance into a target DataBaseSource instance.
	 * 
	 * @param source
	 *            the DataBaseSource instance containing the new values to be merged into the target entity.
	 * @param target
	 *            the DataBaseSource instance to be updated with the values from the source.
	 * @return the updated DataBaseSource instance with the merged properties.
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "version", ignore = true)
	DataBaseSource mergeDataBaseSource(DataBaseSource source, @MappingTarget DataBaseSource target);

	/**
	 * Merges the properties of a source FileSource instance into a target {@link FileSource} instance.
	 * 
	 * @param source
	 *            the FileSource instance containing the new values to be merged into the target entity.
	 * @param target
	 *            the FileSource instance to be updated with the values from the source.
	 * @return the updated FileSource instance with the merged properties.
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "version", ignore = true)
	FileSource mergeFileSource(FileSource source, @MappingTarget FileSource target);

}
