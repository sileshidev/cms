package com.cms.audit.service.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class AuditEvent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Instant timestamp;
	private String actor;
	private String action;
	private String entityType;
	private String entityId;
	@Column(length = 4000)
	private String details;

	public AuditEvent() {}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public Instant getTimestamp() { return timestamp; }
	public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

	public String getActor() { return actor; }
	public void setActor(String actor) { this.actor = actor; }

	public String getAction() { return action; }
	public void setAction(String action) { this.action = action; }

	public String getEntityType() { return entityType; }
	public void setEntityType(String entityType) { this.entityType = entityType; }

	public String getEntityId() { return entityId; }
	public void setEntityId(String entityId) { this.entityId = entityId; }

	public String getDetails() { return details; }
	public void setDetails(String details) { this.details = details; }
}
