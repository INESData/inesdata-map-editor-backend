package com.inesdatamap.mapperbackend.model.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ObjectMap db entity representation
 *
 * @author gmv
 *
 */
@Getter
@Setter
@ToString
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
	 * The object map literalValue.
	 */
	@Column(name = "literal_value")
	private String literalValue;

	/**
	 * The object map value.
	 */
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "object_map_id")
	private List<ObjectMap> objectValue = new ArrayList<>();

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
	 * @return the literalValue
	 */
	public String getLiteralValue() {
		return this.literalValue;
	}

	/**
	 * @param literalValue
	 *            the literalValue to set
	 */
	public void setLiteralValue(String literalValue) {
		this.literalValue = literalValue;
	}

	/**
	 * @return the objectValue
	 */

	public List<ObjectMap> getObjectValue() {
		return new ArrayList<>(this.objectValue);
	}

	/**
	 * @param objectValue
	 *            the objectValue to set
	 */
	public void setObjectValue(List<ObjectMap> objectValue) {
		this.objectValue = new ArrayList<>(objectValue);
	}

}
