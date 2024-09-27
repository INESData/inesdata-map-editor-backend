package com.inesdatamap.mapperbackend.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.rdf4j.model.BNode;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.inesdatamap.mapperbackend.exceptions.RmlWriteException;
import com.inesdatamap.mapperbackend.model.dto.MappingDTO;
import com.inesdatamap.mapperbackend.model.dto.PredicateObjectMapDTO;
import com.inesdatamap.mapperbackend.model.dto.SearchMappingDTO;
import com.inesdatamap.mapperbackend.model.enums.DataFileTypeEnum;
import com.inesdatamap.mapperbackend.model.enums.DataSourceTypeEnum;
import com.inesdatamap.mapperbackend.model.jpa.DataSource;
import com.inesdatamap.mapperbackend.model.jpa.Execution;
import com.inesdatamap.mapperbackend.model.jpa.FileSource;
import com.inesdatamap.mapperbackend.model.jpa.Mapping;
import com.inesdatamap.mapperbackend.model.jpa.MappingField;
import com.inesdatamap.mapperbackend.model.jpa.Ontology;
import com.inesdatamap.mapperbackend.model.mappers.MappingMapper;
import com.inesdatamap.mapperbackend.model.mappers.PredicateObjectMapMapper;
import com.inesdatamap.mapperbackend.properties.AppProperties;
import com.inesdatamap.mapperbackend.repositories.jpa.DataSourceRepository;
import com.inesdatamap.mapperbackend.repositories.jpa.FileSourceRepository;
import com.inesdatamap.mapperbackend.repositories.jpa.MappingRepository;
import com.inesdatamap.mapperbackend.repositories.jpa.OntologyRepository;
import com.inesdatamap.mapperbackend.services.ExecutionService;
import com.inesdatamap.mapperbackend.services.GraphEngineService;
import com.inesdatamap.mapperbackend.services.MappingService;
import com.inesdatamap.mapperbackend.utils.Constants;
import com.inesdatamap.mapperbackend.utils.FileUtils;
import com.inesdatamap.mapperbackend.utils.RmlUtils;

import jakarta.persistence.EntityNotFoundException;

/**
 * Implementation of the MappingService interface.
 */
@Service
public class MappingServiceImpl implements MappingService {

	@Autowired
	private MappingRepository mappingRepo;

	@Autowired
	private GraphEngineService graphEngineService;

	@Autowired
	private ExecutionService executionService;

	@Autowired
	private MappingMapper mappingMapper;

	@Autowired
	private PredicateObjectMapMapper predicateObjectMapMapper;

	@Autowired
	private OntologyRepository ontologyRepository;

	@Autowired
	private DataSourceRepository<DataSource> dataSourceRepository;

	@Autowired
	private FileSourceRepository fileSourceRepository;

	@Autowired
	private AppProperties appProperties;

	/**
	 * Logger
	 */
	protected final Log logger = LogFactory.getLog(this.getClass());

	/**
	 * Retrieves a list of all mappings and maps them to their corresponding DTOs.
	 *
	 * @param pageable
	 * 	pageable
	 *
	 * @return List of MappingsDTOs
	 */
	@Override
	public Page<SearchMappingDTO> listMappings(Pageable pageable) {

		// Get all mappings
		Page<Mapping> mappingsPage = this.mappingRepo.findAll(pageable);

		List<SearchMappingDTO> searchMappingList = new ArrayList<>();

		// Iterate mappings page
		for (Mapping mapping : mappingsPage) {
			SearchMappingDTO searchMapping = new SearchMappingDTO();
			searchMapping.setId(mapping.getId());
			searchMapping.setName(mapping.getName());

			List<String> ontologies = new ArrayList<>();
			List<String> dataSources = new ArrayList<>();

			// Iterate fields list and set values to ontologies and sources list
			for (MappingField field : mapping.getFields()) {
				if (field.getOntology() != null && field.getOntology().getName() != null) {
					ontologies.add(field.getOntology().getName());
				}
				if (field.getSource() != null && field.getSource().getName() != null) {
					dataSources.add(field.getSource().getName());
				}
			}
			// Set ontologies and sources lists to DTO
			searchMapping.setOntologies(ontologies);
			searchMapping.setDataSources(dataSources);

			// Set DTO to list
			searchMappingList.add(searchMapping);
		}

		// Convert from list to Page
		return new PageImpl<>(searchMappingList, pageable, mappingsPage.getTotalElements());

	}

	/**
	 * Deletes a mapping by its id.
	 *
	 * @param id
	 * 	the ID of the mapping to delete
	 */
	@Override
	public void deleteMapping(Long id) {

		// Get entity if exists
		Mapping mapping = this.getEntity(id);

		// Delete files related to the mapping
		this.deleteMappingFiles(mapping);

		// Delete mapping
		this.mappingRepo.deleteById(id);

	}

	/**
	 * Retrieves a MappingField entity by its ID.
	 *
	 * @param id
	 * 	the ID of the MappingField to retrieve
	 *
	 * @return the MappingField entity corresponding to the given ID
	 */
	@Override
	public Mapping getEntity(Long id) {
		return this.mappingRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id));
	}

	/**
	 * Creates a new mapping.
	 *
	 * @param mappingDTO
	 * 	the mapping to create
	 *
	 * @return the created mapping
	 */
	@Override
	public Mapping create(MappingDTO mappingDTO) {

		Mapping mapping = setRelationships(mappingDTO);

		byte[] rml = buildRml(mapping);
		mapping.setRml(rml);

		return mapping;
	}

	/**
	 * Saves a mapping.
	 *
	 * @param mapping
	 * 	the mapping to save
	 *
	 * @return the saved mapping
	 */
	@Override
	public MappingDTO save(Mapping mapping) {
		return this.mappingMapper.entityToDto(this.mappingRepo.save(mapping));
	}

	/**
	 * Sets the relationships of a mapping.
	 *
	 * @param mappingDTO
	 * 	the mapping dto
	 *
	 * @return the mapping with the relationships set
	 */
	private Mapping setRelationships(MappingDTO mappingDTO) {

		Mapping mapping = mappingMapper.dtoToEntity(mappingDTO);

		if (!CollectionUtils.isEmpty(mapping.getFields())) {

			mapping.getFields().forEach(field -> {

				Ontology ontology = this.ontologyRepository.getReferenceById(field.getOntology().getId());
				DataSource dataSource = this.dataSourceRepository.getReferenceById(field.getSource().getId());

				field.setOntology(ontology);
				field.setSource(dataSource);

			});
		}

		return mapping;
	}

	/**
	 * Builds the RML for a mapping.
	 *
	 * @param mapping
	 * 	the mapping to build the RML for
	 *
	 * @return the RML for the mapping
	 */
	private byte[] buildRml(Mapping mapping) {

		ModelBuilder builder = new ModelBuilder();
		SimpleValueFactory vf = SimpleValueFactory.getInstance();
		byte[] rmlContent;

		// TODO: ¿En función de qué va?
		String baseUri = "http://example.org/";
		setNamespaces(builder, baseUri);

		mapping.getFields().forEach(field -> {

			// Every field is a triples map

			BNode mappingNode = vf.createBNode();

			// Logical source or logical table
			if (field.getSource().getType().equals(DataSourceTypeEnum.FILE)) {
				FileSource fileSource = fileSourceRepository.getReferenceById(field.getSource().getId());
				createLogicalSource(builder, mappingNode, fileSource);
			}

			// Subject map
			RmlUtils.createSubjectMapNode(builder, mappingNode, field.getSubject().getTemplate(), field.getSubject().getClassName());

			// Predicate-object maps
			field.getPredicates().forEach(predicate -> {
				PredicateObjectMapDTO predicateObjectMapDTO = predicateObjectMapMapper.entityToDto(predicate);
				RmlUtils.createPredicateObjectMapNode(builder, mappingNode, predicate.getPredicate(), predicateObjectMapDTO.getObjectMap());
			});

		});

		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Rio.write(builder.build(), out, baseUri, RDFFormat.TURTLE);

			rmlContent = out.toByteArray();
			logger.info(new String(rmlContent, StandardCharsets.UTF_8));

		} catch (URISyntaxException e) {
			throw new RmlWriteException(e.getMessage(), e);
		}

		return rmlContent;

	}

	/**
	 * Sets the namespaces for the RML.
	 *
	 * @param builder
	 * 	the model builder
	 * @param baseUri
	 * 	the base URI
	 */
	private static void setNamespaces(ModelBuilder builder, String baseUri) {

		// Define namespaces and base IRI
		builder.setNamespace("rr", "http://www.w3.org/ns/r2rml#")

			.setNamespace("rml", "http://semweb.mmlab.be/ns/rml#")

			.setNamespace("ql", "http://semweb.mmlab.be/ns/ql#")

			.setNamespace("xsd", "http://www.w3.org/2001/XMLSchema#")

			.setNamespace("ex", baseUri);

	}

	/**
	 * Creates a logical source node.
	 *
	 * @param builder
	 * 	the model builder
	 * @param mappingNode
	 * 	the parent mapping node
	 * @param source
	 * 	the source
	 */
	private static void createLogicalSource(ModelBuilder builder, BNode mappingNode, FileSource source) {

		if (source.getFileType().equals(DataFileTypeEnum.CSV)) {
			String sourcePath = String.join(File.separator, source.getFilePath(), source.getFileName());
			RmlUtils.createCsvLogicalSourceNode(builder, mappingNode, sourcePath);
		}

	}

	/**
	 * Materializes a mapping by its id.
	 *
	 * @param id
	 * 	the ID of the mapping to materialize
	 *
	 * @return the results of the materialization
	 */
	@Override
	public List<String> materialize(Long id) {

		Mapping mapping = this.getEntity(id);
		List<String> results;

		OffsetDateTime now = OffsetDateTime.now();

		String mappingFilePath = getMappingFilePath(id, now);

		String knowledgeGraphOutputFilePath = getKnowledgeGraphOutputFilePath(id, now);

		String logFilePath = getLogFilePath(id, now);

		// Create mapping file
		File mappingFile = FileUtils.createFile(mapping.getRml(), mappingFilePath);

		saveExecution(mapping, now, mappingFilePath, knowledgeGraphOutputFilePath, logFilePath);

		results = this.graphEngineService.run(mappingFile.getAbsolutePath(), knowledgeGraphOutputFilePath, logFilePath);

		return results;
	}

	/**
	 * Gets the mapping file path.
	 *
	 * @param mappingId
	 * 	the mapping id
	 * @param executionTime
	 * 	the execution time
	 *
	 * @return the mapping file path
	 */
	private String getMappingFilePath(Long mappingId, OffsetDateTime executionTime) {
		return FileUtils.getFilePathFromOutputDirectory(appProperties.getDataProcessingPath(), mappingId, executionTime,
			Constants.MAPPING_FILE_NAME);
	}

	/**
	 * Gets the knowledge graph output file path.
	 *
	 * @param mappingId
	 * 	the mapping id
	 * @param executionTime
	 * 	the execution time
	 *
	 * @return the knowledge graph output file path
	 */
	private String getKnowledgeGraphOutputFilePath(Long mappingId, OffsetDateTime executionTime) {
		return FileUtils.getFilePathFromOutputDirectory(appProperties.getDataProcessingPath(), mappingId, executionTime,
			Constants.KG_OUTPUT_FILE_NAME);
	}

	/**
	 * Gets the log file path.
	 *
	 * @param mappingId
	 * 	the mapping id
	 * @param executionTime
	 * 	the execution time
	 *
	 * @return the log file path
	 */
	private String getLogFilePath(Long mappingId, OffsetDateTime executionTime) {
		return FileUtils.getFilePathFromOutputDirectory(appProperties.getDataProcessingPath(), mappingId, executionTime,
			Constants.GRAPH_ENGINE_LOG_FILE_NAME);
	}

	/**
	 * Saves an execution.
	 *
	 * @param mapping
	 * 	the mapping
	 * @param executionDateTime
	 * 	the execution date time
	 * @param mappingFilePath
	 * 	the mapping file path
	 * @param knowledgeGraphOutputFilePath
	 * 	the knowledge graph output file path
	 * @param logFilePath
	 * 	the log file path
	 */
	private void saveExecution(Mapping mapping, OffsetDateTime executionDateTime, String mappingFilePath,
		String knowledgeGraphOutputFilePath, String logFilePath) {

		Execution execution = new Execution();

		execution.setExecutionDate(executionDateTime);
		execution.setMappingFileName(Paths.get(mappingFilePath).getFileName().toString());
		execution.setKnowledgeGraphFileName(Paths.get(knowledgeGraphOutputFilePath).getFileName().toString());
		execution.setLogFileName(Paths.get(logFilePath).getFileName().toString());

		execution.setMapping(mapping);

		this.executionService.save(execution);

	}

	/**
	 * Deletes the mapping files.
	 *
	 * @param mapping
	 * 	the mapping
	 */
	private void deleteMappingFiles(Mapping mapping) {

		String executionsFolderPath = String.join(File.separator, appProperties.getDataProcessingPath(), Constants.DATA_OUTPUT_FOLDER_NAME,
			mapping.getId().toString());

		FileUtils.deleteDirectory(Paths.get(executionsFolderPath));

	}

}