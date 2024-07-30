package com.spinifexit.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spinifexit.model.support.Cuid;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Data
@Entity
@Table(name = "models")
public class Model {

	public Model() {
		// default constructor logic goes here, if any
	}

	@Id
	@Column(columnDefinition = "text")
	private String id;

	@Column(nullable = false)
	private Date createdAt;

	@Column(nullable = false, columnDefinition = "text")
	private String tenant;

	public Model(String tenant) {
		this.tenant = tenant;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the createdAt
	 */
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return the tenant
	 */
	public String getTenant() {
		return tenant;
	}

	/**
	 * @param tenant the tenant to set
	 */
	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

	@PrePersist
	protected void prePersist() {
		id = Cuid.createCuid();
		createdAt = new Date();
	}
}