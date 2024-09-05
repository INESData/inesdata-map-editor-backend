package com.inesdatamap.mapperbackend.model.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.inesdatamap.mapperbackend.model.dto.FileSourceDTO;
import com.inesdatamap.mapperbackend.model.jpa.FileSource;

/**
 * Mapper interface for converting between FileSource and FileSourceDTO.
 */
@Mapper(componentModel = "spring", uses = BaseEntityMapper.class)
public interface FileSourceMapper extends BaseEntityMapper<FileSourceDTO, FileSource> {

	/**
	 * Converts a FileSource entity to a FileSourceDTO.
	 *
	 * @param savedFileSource
	 * 	The FileSource
	 *
	 * @return The FileSourceDTO
	 */
	FileSourceDTO toDTO(FileSource savedFileSource);

	/**
	 * Converts a FileSourceDTO to a FileSource entity.
	 *
	 * @param fileSourceDTO
	 * 	The FileSourceDTO
	 *
	 * @return The FileSource
	 */
	@Override
	@Mapping(target = "filePath", ignore = true)
	FileSource dtoToEntity(FileSourceDTO fileSourceDTO);

	/**
	 * Merges the values from a FileSourceDTO into an existing FileSource entity.
	 *
	 * @param fileSourceDTO
	 * 	The FileSourceDTO
	 * @param fileSource
	 * 	The FileSource entity to be updated
	 */
	@Mapping(target = "filePath", ignore = true)
	void merge(FileSourceDTO fileSourceDTO, @MappingTarget FileSource fileSource);

}
