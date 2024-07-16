package com.inesdatamap.mapperbackend.model.jpa;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * MappingRule db entity representation
 *
 * @author gmv
 *
 */
@Entity
@Table(name = "MAPPING_RULE")
public class MappingRule extends BaseEntity implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -9026183579451737687L;

	/**
	 * The data source associated with the mapping.
	 */
	@Column(name = "source")
	private DataSource source;

	/**
	 * The ontology associated with the mapping.
	 */
	@Column(name = "ontology")
	private Ontology ontology;

	/**
	 * The field from the source used in the mapping.
	 */
	@Column(name = "source_field")
	private MappingField sourceField;

	/**
	 * The field from the target used in the mapping.
	 */
	@Column(name = "target_field")
	private MappingField targetField;

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

	/**
	 * @return the sourceField
	 */
	public MappingField getSourceField() {
		return this.sourceField;
	}

	/**
	 * @param sourceField
	 *            the sourceField to set
	 */
	public void setSourceField(MappingField sourceField) {
		this.sourceField = sourceField;
	}

	/**
	 * @return the targetField
	 */
	public MappingField getTargetField() {
		return this.targetField;
	}

	/**
	 * @param targetField
	 *            the targetField to set
	 */
	public void setTargetField(MappingField targetField) {
		this.targetField = targetField;
	}

}
