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
@Table(name = "OBJECT_MAP")
public class ObjectMap extends BaseEntity implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The object map key.
	 */
	@Column(name = "key")
	private String key;

	/**
	 * The object map value.
	 */
	@Column(name = "valu")
	private ObjectMap value;

	/**
	 * @return the key
	 */
	public String getKey() {
		return this.key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the value
	 */
	public ObjectMap getValue() {
		return this.value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(ObjectMap value) {
		this.value = value;
	}

}
