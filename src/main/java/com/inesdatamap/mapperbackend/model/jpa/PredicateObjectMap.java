package com.inesdatamap.mapperbackend.model.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "predicate_object_map_id")
	private List<ObjectMap> objectMap = new ArrayList<>();

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
	public List<ObjectMap> getObjectMap() {
		return new ArrayList<>(this.objectMap);
	}

	/**
	 * @param objectMap
	 *            the objectMap to set
	 */
	public void setObjectMap(List<ObjectMap> objectMap) {
		this.objectMap = new ArrayList<>(objectMap);
	}

}
