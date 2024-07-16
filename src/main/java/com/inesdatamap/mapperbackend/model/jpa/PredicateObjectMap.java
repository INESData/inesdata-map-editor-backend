package com.inesdatamap.mapperbackend.model.jpa;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * ObjectMap db entity representation
 *
 * @author gmv
 *
 */
@Entity
@Table(name = "PREDICATE_OBJECT_MAP")
public class PredicateObjectMap extends BaseEntity implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6690768617017394788L;

	/**
	 * The predicate in the mapping.
	 */
	@Column(name = "predicate")
	private String predicate;

	/**
	 * The object map associated with the predicate.
	 */
	@Column(name = "object_map")
	private ObjectMap objectMap;

	/**
	 * @return the predicate
	 */
	public String getPredicate() {
		return this.predicate;
	}

	/**
	 * @param predicate
	 *            the predicate to set
	 */
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	/**
	 * @return the objectMap
	 */
	public ObjectMap getObjectMap() {
		return this.objectMap;
	}

	/**
	 * @param objectMap
	 *            the objectMap to set
	 */
	public void setObjectMap(ObjectMap objectMap) {
		this.objectMap = objectMap;
	}

}
