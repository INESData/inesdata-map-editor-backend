package com.inesdatamap.mapperbackend.model.jpa;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Namespace db entity representation
 *
 * @author gmv
 *
 */
@Entity
@Table(name = "NAMESPACE")
public class Namespace extends BaseEntity implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4721858364453228281L;

	/**
	 * The prefix of the namespace.
	 */
	@Column(name = "prefix")
	private String prefix;

	/**
	 * The iri of the namespace.
	 */
	@Column(name = "iri")
	private String iri;

	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return this.prefix;
	}

	/**
	 * @param prefix
	 *            the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * @return the iri
	 */
	public String getIri() {
		return this.iri;
	}

	/**
	 * @param iri
	 *            the iri to set
	 */
	public void setIri(String iri) {
		this.iri = iri;
	}

}
