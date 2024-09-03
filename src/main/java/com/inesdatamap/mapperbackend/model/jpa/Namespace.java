package com.inesdatamap.mapperbackend.model.jpa;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Namespace db entity representation
 *
 * @author gmv
 *
 */
@Getter
@Setter
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
	 * The mapping associated with the namespace.
	 */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "mapping_id")
	private Mapping mapping;

}
