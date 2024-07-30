package com.spinifexit.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

@Entity
@Table(name = "tenant_config", schema = "central")
@NamedQueries({
		@NamedQuery(name = TenantConfig.FINDBY_TENANT_URL,
				query = "select t from TenantConfigs t where t.tenantUrl = :tenantUrl and t.status = com.spinifexit.model.TenantStatus.ACTIVE"),
		@NamedQuery(name = TenantConfig.FIND_ALL,
				query = "select t from TenantConfig t where t.status = com.spinifexit.model.TenantStatus.ACTIVE")
})
public class TenantConfig {

	public static final String FINDBY_TENANT_URL = "TenantConfig.findByTenantURL";

	public static final String FIND_ALL = "TenantConfig.findAllActiveTenants";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private TenantStatus status;

	@Column(name = "tenantUrl")
	private String tenantUrl;

	@Column(name = "stage")
	private String stage;

	@Column(name = "schemaName")
	private String schemaName;

	@ManyToOne
	@JoinColumn(name = "dbId")
	private DBConnection dbConnection;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the status
	 */
	public TenantStatus getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(TenantStatus status) {
		this.status = status;
	}

	/**
	 * @return the tenantUrl
	 */
	public String getTenantUrl() {
		return tenantUrl;
	}

	/**
	 * @param tenantUrl
	 *            the tenantUrl to set
	 */
	public void setTenantUrl(String tenantUrl) {
		this.tenantUrl = tenantUrl;
	}

	/**
	 * @return the stage
	 */
	public String getStage() {
		return stage;
	}

	/**
	 * @param stage
	 *            the stage to set
	 */
	public void setStage(String stage) {
		this.stage = stage;
	}

	/**
	 * @return the schema
	 */
	public String getSchemaName() {
		return schemaName;
	}

	/**
	 * @param schema
	 *            the schema to set
	 */
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	/**
	 * @return the dbConnection
	 */
	public DBConnection getDbConnection() {
		return dbConnection;
	}

	/**
	 * @param dbConnection
	 *            the dbConnection to set
	 */
	public void setDbConnection(DBConnection dbConnection) {
		this.dbConnection = dbConnection;
	}

	/**
	 * @return the findbyTenantUrl
	 */
	public static String getFindbyTenantUrl() {
		return FINDBY_TENANT_URL;
	}

}
