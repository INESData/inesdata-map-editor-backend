package com.inesdatamap.mapperbackend.utils;

import org.eclipse.rdf4j.model.BNode;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;

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
	 * @param reference
	 * 	the reference for the object map
	 * @param datatype
	 * 	the data type IRI for the object map (see {@link org.eclipse.rdf4j.model.vocabulary.XSD})
	 */
	public static void createPredicateObjectMapNode(ModelBuilder builder, Resource mappingNode, String predicate, String reference,
		IRI datatype) {
		SimpleValueFactory vf = SimpleValueFactory.getInstance();

		BNode predicateObjectMapNode = vf.createBNode();
		BNode objectMapNode = vf.createBNode();
		builder.subject(mappingNode)
			// rr:predicateObjectMap
			.add("rr:predicateObjectMap", predicateObjectMapNode);
		builder.subject(predicateObjectMapNode)
			// rr:predicate
			.add("rr:predicate", vf.createIRI(predicate))
			// rr:objectMap
			.add("rr:objectMap", objectMapNode);
		builder.subject(objectMapNode)
			// rml:reference
			.add("rml:reference", vf.createLiteral(reference));

		if (datatype != null) {
			builder.subject(objectMapNode).add("rr:datatype", datatype);
		}
	}

}
