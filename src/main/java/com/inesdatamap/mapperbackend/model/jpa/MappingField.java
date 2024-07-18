package com.inesdatamap.mapperbackend.model.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * MappingField db entity representation
 *
 * @author gmv
 *
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "MAPPING_FIELD")
public class MappingField extends BaseEntity implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8225534420024816076L;

	/**
	 * The logical table associated with the mapping field.
	 */
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "logical_table_id")
	private LogicalTable logicalTable;

	/**
	 * The logical source associated with the mapping field.
	 */
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "logical_source_id")
	private LogicalSource logicalSource;

	/**
	 * The subject associated with the mapping field.
	 */
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "subject_id")
	private SubjectMap subject;

	/**
	 * The predicates associated with the mapping field.
	 */
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "mapping_field_id")
	private List<PredicateObjectMap> predicates = new ArrayList<>();

	/**
	 * The source associated with the mapping field.
	 */
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "data_source_id")
	private DataSource source;

	/**
	 * The ontology associated with the mapping field.
	 */
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "ontology_id")
	private Ontology ontology;

	/**
	 * @return the logicalTable
	 */

	public LogicalTable getLogicalTable() {
		return this.logicalTable;
	}

	/**
	 * @param logicalTable
	 *            the logicalTable to set
	 */

	public void setLogicalTable(LogicalTable logicalTable) {
		this.logicalTable = logicalTable;
	}

	/**
	 * @return the logicalSource
	 */

	public LogicalSource getLogicalSource() {
		return this.logicalSource;
	}

	/**
	 * @param logicalSource
	 *            the logicalSource to set
	 */

	public void setLogicalSource(LogicalSource logicalSource) {
		this.logicalSource = logicalSource;
	}

	/**
	 * @return the subject
	 */

	public SubjectMap getSubject() {
		return this.subject;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */

	public void setSubject(SubjectMap subject) {
		this.subject = subject;
	}

	/**
	 * @return the predicates
	 */
	public List<PredicateObjectMap> getPredicates() {
		return new ArrayList<>(this.predicates);
	}

	/**
	 * @param predicates
	 *            the predicates to set The ontology associated with the mapping field.
	 */
	public void setPredicates(List<PredicateObjectMap> predicates) {
		this.predicates = new ArrayList<>(predicates);
	}

	/**
	 * @return the source
	 */

	public DataSource getSource() {
		return this.source;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(DataSource source) {
		this.source = source;
	}

	/**
	 * @return the ontology
	 */

	public Ontology getOntology() {
		return this.ontology;
	}

	/**
	 * @param ontology
	 *            the ontology to set
	 */
	public void setOntology(Ontology ontology) {
		this.ontology = ontology;
	}

}
