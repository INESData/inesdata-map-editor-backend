package com.inesdatamap.mapperbackend.model.jpa;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Data Base Source db entity representation
 *
 * @author gmv
 *
 */
@Entity
public class DataBaseSource extends DataSource implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2425750964496627584L;

	/**
	 * The connection string for the database source.
	 */
	@Column(name = "connection_string")
	private String connectionString;

	/**
	 * The user name for accessing the database.
	 */
	@Column(name = "user")
	private String user;

	/**
	 * The password for accessing the database.
	 */
	@Column(name = "password")
	private String password;

	/**
	 * The type of the database.
	 */
	@JoinColumn(name = "database_type")
	@ManyToOne(fetch = FetchType.LAZY)
	private DataBaseType databaseType;

	/**
	 * @return the connectionString
	 */
	public String getConnectionString() {
		return this.connectionString;
	}

	/**
	 * @param connectionString
	 *            the connectionString to set
	 */
	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return this.user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the databaseType
	 */
	public DataBaseType getDatabaseType() {
		return this.databaseType;
	}

	/**
	 * @param databaseType
	 *            the databaseType to set
	 */
	public void setDatabaseType(DataBaseType databaseType) {
		this.databaseType = databaseType;
	}

}
