package com.inesdatamap.mapperbackend.model.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * MappingField db entity representation
 *
 * @author gmv
 *
 */
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
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "logical_table_id")
	private LogicalTable logicalTable;

	/**
	 * The logical source associated with the mapping field.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "logical_source_id")
	private LogicalSource logicalSource;

	/**
	 * The subject associated with the mapping field.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subject_id")
	private Subject subject;

	/**
	 * The predicates associated with the mapping field.
	 */
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "mapping_field_id")
	private List<PredicateObjectMap> predicates = new ArrayList<>();

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
	public Subject getSubject() {
		return this.subject;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(Subject subject) {
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
	 *            the predicates to set
	 */
	public void setPredicates(List<PredicateObjectMap> predicates) {
		this.predicates = new ArrayList<>(predicates);
	}

}
