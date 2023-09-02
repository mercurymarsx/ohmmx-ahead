package com.ohmmx.common.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import com.ohmmx.common.utils.DateUtils;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "TSK_QUEUE")
@Cacheable(false)
public class TaskQueue extends TimestampObject<TaskQueue.ID> {

	@Embeddable
	public static class ID implements Serializable {

		private String type;
		private String reference;

		public ID() {
		}

		public ID(String type, String reference) {
			this.type = type;
			this.reference = reference;
		}

		@Column(name = "TYPE", length = 128)
		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		@Column(name = "REFERENCE")
		public String getReference() {
			return reference;
		}

		public void setReference(String reference) {
			this.reference = reference;
		}

		@Override
		public String toString() {
			return type + ":" + reference;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((reference == null) ? 0 : reference.hashCode());
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ID other = (ID) obj;
			if (!Objects.equals(reference, other.reference)) {
				return false;
			}
			if (!Objects.equals(type, other.type)) {
				return false;
			}
			return true;
		}
	}

	private ID id;

	@EmbeddedId
	@Override
	public ID getId() {
		return id;
	}

	@Override
	public void setId(ID id) {
		this.id = id;
	}

	public TaskQueue() {
	}

	public TaskQueue(ID id) {
		this.id = id;
	}

	public TaskQueue(String type, String reference) {
		this(new ID(type, reference));
	}

	public TaskQueue(String type, String reference, String state) {
		this(type, reference);
		this.state = state;
	}

	private String lockedBy;

	@Column(name = "LOCKED_BY", length = 32)
	public String getLockedBy() {
		return lockedBy;
	}

	public void setLockedBy(String lockedBy) {
		this.lockedBy = lockedBy;
	}

	private String state = "NEW";

	@Column(name = "STATE", length = 16)
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	private String statePrevious;

	private String stateHistory = "";

	private int retries;

	private Date lastRun;

	private Date notBefore = new Date();

	private Date notAfter = DateUtils.nextDay(new Date());

	private int ttl = 255;

	private int tags;

	@Column(name = "ST_PREV", length = 16)
	public String getStatePrevious() {
		return statePrevious;
	}

	public void setStatePrevious(String statePrevious) {
		this.statePrevious = statePrevious;
	}

	@Column(name = "ST_HIST")
	@Lob
	public String getStateHistory() {
		return stateHistory;
	}

	public void setStateHistory(String stateHistory) {
		this.stateHistory = stateHistory;
	}

	@Column(name = "CNT_RETRY", precision = 11)
	public int getRetries() {
		return retries;
	}

	public void setRetries(int retries) {
		this.retries = retries;
	}

	@Column(name = "LAST_RUN")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getLastRun() {
		return lastRun;
	}

	public void setLastRun(Date lastRun) {
		this.lastRun = lastRun;
	}

	@Column(name = "NOT_BEFORE")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getNotBefore() {
		return notBefore;
	}

	public void setNotBefore(Date notBefore) {
		this.notBefore = notBefore;
	}

	@Column(name = "NOT_AFTER")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getNotAfter() {
		return notAfter;
	}

	public void setNotAfter(Date notAfter) {
		this.notAfter = notAfter;
	}

	@Column(name = "TTL", precision = 11)
	public int getTtl() {
		return ttl;
	}

	public void setTtl(int ttl) {
		this.ttl = ttl;
	}

	@Column(name = "TAGS", precision = 11)
	public int getTags() {
		return tags;
	}

	public void setTags(int tags) {
		this.tags = tags;
	}
}
