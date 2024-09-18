package com.inesdatamap.mapperbackend.utils;

import java.util.List;

import org.eclipse.rdf4j.model.BNode;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;

import com.inesdatamap.mapperbackend.model.dto.ObjectMapDTO;

/**
 * Utility class for RML.
 */
public final class RmlUtils {

	/**
	 * Private constructor to prevent instantiation.
	 */
	private RmlUtils() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Create a CSV logical source node.
	 *
	 * @param builder
	 * 	the model builder
	 * @param mappingNode
	 * 	the parent mapping node
	 * @param source
	 * 	the path to the CSV file
	 */
	public static void createCsvLogicalSourceNode(ModelBuilder builder, Resource mappingNode, String source) {
		SimpleValueFactory vf = SimpleValueFactory.getInstance();

		BNode logicalSourceNode = vf.createBNode();
		builder.subject(mappingNode)
			// rml:logicalSource
			.add("rml:logicalSource", logicalSourceNode);
		builder.subject(logicalSourceNode)
			// rml:source
			.add("rml:source", vf.createLiteral(source))
			// rml:referenceFormulation
			.add("rml:referenceFormulation", vf.createIRI("ql:CSV"));
	}

	/**
	 * Create a subjectMap node.
	 *
	 * @param builder
	 * 	the model builder
	 * @param mappingNode
	 * 	the parent mapping node
	 * @param template
	 * 	the template
	 * @param clazz
	 * 	the class IRI
	 */
	public static void createSubjectMapNode(ModelBuilder builder, Resource mappingNode, String template, String clazz) {
		SimpleValueFactory vf = SimpleValueFactory.getInstance();

		BNode subjectMapNode = vf.createBNode();
		builder.subject(mappingNode)
			// rr:subjectMap
			.add("rr:subjectMap", subjectMapNode);
		builder.subject(subjectMapNode)
			// rr:template
			.add("rr:template", vf.createLiteral(template))
			// rr:class
			.add("rr:class", vf.createIRI(clazz));
	}

	/**
	 * Create a predicate-object map node.
	 *
	 * @param builder
	 * 	the model builder
	 * @param mappingNode
	 * 	the parent mapping node
	 * @param predicate
	 * 	the predicate
	 * @param objectMap
	 * 	the object map
	 */
	public static void createPredicateObjectMapNode(ModelBuilder builder, Resource mappingNode, String predicate,
		List<ObjectMapDTO> objectMap) {
		SimpleValueFactory vf = SimpleValueFactory.getInstance();

		BNode predicateObjectMapNode = vf.createBNode();
		builder.subject(mappingNode)
			// rr:predicateObjectMap
			.add("rr:predicateObjectMap", predicateObjectMapNode);
		builder.subject(predicateObjectMapNode)
			// rr:predicate
			.add("rr:predicate", vf.createIRI(predicate));

		Resource objectMapNode = createObjectMapNode(builder, vf.createBNode(), objectMap);

		builder.subject(predicateObjectMapNode).add("rr:objectMap", objectMapNode);
	}

	/**
	 * Create an object map node.
	 *
	 * @param builder
	 * 	the model builder
	 * @param parentNode
	 * 	the parent node
	 * @param objectMapDTO
	 * 	the object map DTO
	 *
	 * @return the object map node
	 */
	public static Resource createObjectMapNode(ModelBuilder builder, Resource parentNode, List<ObjectMapDTO> objectMapDTO) {

		SimpleValueFactory vf = SimpleValueFactory.getInstance();

		objectMapDTO.forEach(objectMap -> {

			if (objectMap.getLiteralValue() != null) {
				builder.subject(parentNode).add(objectMap.getKey(), vf.createLiteral(objectMap.getLiteralValue()));
			}

			if (objectMap.getObjectValue() != null) {

				BNode nestedObjectMapNode = vf.createBNode();

				builder.subject(parentNode).add(objectMap.getKey(),
					createObjectMapNode(builder, nestedObjectMapNode, objectMap.getObjectValue()));
			}

		});

		return parentNode;
	}

}
