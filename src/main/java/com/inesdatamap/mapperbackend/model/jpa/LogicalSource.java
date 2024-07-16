package com.inesdatamap.mapperbackend.model.jpa;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Logical source entity representation
 *
 * @author gmv
 *
 */
@Entity
@Table(name = "LOGICAL_SOURCE")
public class LogicalSource extends BaseEntity implements Serializable {

	/**
	 * serialVersionUID for serialization
	 */
	private static final long serialVersionUID = 2258710450708360140L;

	/**
	 * The source of the logical source.
	 */
	@Column(name = "source")
	private String source;

	/**
	 * The reference formulation of the logical source.
	 */
	@Column(name = "reference_formulation")
	private String referenceFormulation;

	/**
	 * The iterator of the logical source.
	 */
	@Column(name = "iterator")
	private String iterator;

	/**
	 * The query associated with the logical source.
	 */
	@Column(name = "query")
	private String query;

	/**
	 * @return the source
	 */
	public String getSource() {
		return this.source;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return the referenceFormulation
	 */
	public String getReferenceFormulation() {
		return this.referenceFormulation;
	}

	/**
	 * @param referenceFormulation
	 *            the referenceFormulation to set
	 */
	public void setReferenceFormulation(String referenceFormulation) {
		this.referenceFormulation = referenceFormulation;
	}

	/**
	 * @return the iterator
	 */
	public String getIterator() {
		return this.iterator;
	}

	/**
	 * @param iterator
	 *            the iterator to set
	 */
	public void setIterator(String iterator) {
		this.iterator = iterator;
	}

	/**
	 * @return the query
	 */
	public String getQuery() {
		return this.query;
	}

	/**
	 * @param query
	 *            the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
	}

}
