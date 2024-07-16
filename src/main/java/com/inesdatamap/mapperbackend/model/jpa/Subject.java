package com.inesdatamap.mapperbackend.model.jpa;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Subject db entity representation
 *
 * @author gmv
 *
 */
@Entity
@Table(name = "SUBJECT")
public class Subject extends BaseEntity implements Serializable {

	/**
	 * serialVersionUID for serialization
	 */
	private static final long serialVersionUID = -3164348767737374579L;

	/**
	 * The template associated with the subject.
	 */
	@Column(name = "template")
	private String template;

	/**
	 * The class name associated with the subject.
	 */
	@Column(name = "class_name")
	private String className;

	/**
	 * @return the template
	 */
	public String getTemplate() {
		return this.template;
	}

	/**
	 * @param template
	 *            the template to set
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return this.className;
	}

	/**
	 * @param className
	 *            the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

}
