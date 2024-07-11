package com.inesdatamap.mapperbackend.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.StringDocumentSource;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static java.lang.System.out;

@ExtendWith(SpringExtension.class)
class OWLUtilsTest {

	@Test
	void testOwlApi() throws IOException, OWLOntologyCreationException {

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

		InputStream file = getClass().getClassLoader().getResourceAsStream("unesco-thesaurus.ttl");
		String content = new String(file.readAllBytes(), StandardCharsets.UTF_8);

		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new StringDocumentSource(content));

		getOntologyInfo(ontology);

		getClasses(ontology, manager);
	}

	private void getOntologyInfo(OWLOntology owl) {

		out.println(owl.getAxiomCount());
		out.println(owl.axioms(AxiomType.DECLARATION).count());
		out.println(owl.getLogicalAxiomCount());
		out.println(owl.classesInSignature().count());
		out.println(owl.objectPropertiesInSignature().count());
		out.println(owl.dataPropertiesInSignature().count());
		out.println(owl.individualsInSignature().count());
		out.println(owl.annotationPropertiesInSignature().count());

	}

	private void getClasses(OWLOntology owl, OWLOntologyManager manager) {

		// Get all classes in a ontology
		owl.classesInSignature()

				// Map them into a OntologyObjectDto
				//.map(clazz -> getLabel(clazz, owl, manager))
				.map(clazz -> getIndividuals(clazz, owl, manager))

				.forEach(System.out::println);

	}

	private Object getIndividuals(OWLClass clazz, OWLOntology owl, OWLOntologyManager manager) {
		OWLDataFactory factory = manager.getOWLDataFactory();

		List<OWLIndividual> labels = EntitySearcher.getIndividuals(clazz, owl).toList();

		return labels;
	}

	private String getLabel(OWLEntity owlEntity, OWLOntology owl, OWLOntologyManager manager) {

		OWLDataFactory factory = manager.getOWLDataFactory();
		OWLAnnotationProperty rdfsLabel = factory.getRDFSLabel();

		Optional<String> label =

				// Get annotation labels of the entity
				EntitySearcher.getAnnotationObjects(owlEntity, owl, rdfsLabel)

						// Map each annotation as a OWLLiteral
						.map(annotation -> annotation.getValue().asLiteral())

						// Filter the ones present
						.filter(Optional::isPresent)

						// Map the literal to return its value
						.map(value -> value.get().getLiteral())

						.findFirst();

		return label.isPresent() ? label.get() : "";
	}
}
