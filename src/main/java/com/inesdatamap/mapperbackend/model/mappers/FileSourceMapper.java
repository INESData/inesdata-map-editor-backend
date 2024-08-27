package com.inesdatamap.mapperbackend.model.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.inesdatamap.mapperbackend.model.dto.FileSourceDTO;
import com.inesdatamap.mapperbackend.model.jpa.FileSource;

/**
 * Mapper interface for converting between FileSource and FileSourceDTO.
 *
 */
@Mapper(componentModel = "spring", uses = BaseEntityMapper.class)
public interface FileSourceMapper extends BaseEntityMapper<FileSourceDTO, FileSource> {

	/**
	 * Converts a FileSource entity to a FileSourceDTO.
	 *
	 * @param savedFileSource
	 *            The FileSource
	 * @return The FileSourceDTO
	 *
	 */
	FileSourceDTO toDTO(FileSource savedFileSource);

	/**
	 * Converts a FileSourceDTO to a FileSource entity.
	 *
	 * @param fileSourceDTO
	 *            The FileSourceDTO
	 * @return The FileSource
	 *
	 */
	@Override
	FileSource dtoToEntity(FileSourceDTO fileSourceDTO);

	/**
	 * Merges the values from a DataBaseSourceDTO into an existing DataBaseSource entity.
	 *
	 * @param fileSourceDTO
	 *            The FileSourceDTO
	 * @param fileSource
	 *            The FileSource entity to be updated
	 */
	void merge(FileSourceDTO fileSourceDTO, @MappingTarget FileSource fileSource);

}
