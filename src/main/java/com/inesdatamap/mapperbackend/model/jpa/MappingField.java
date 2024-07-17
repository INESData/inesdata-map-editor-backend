package com.inesdatamap.mapperbackend.model.jpa;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
	private SubjectMap subject;

	/**
	 * The predicates associated with the mapping field.
	 */
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "mapping_field_id")
	private List<PredicateObjectMap> predicates;

	/**
	 * The source associated with the mapping field.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "source_id")
	private DataSource source;

	/**
	 * The ontology associated with the mapping field.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ontology_id")
	private Ontology ontology;
}
