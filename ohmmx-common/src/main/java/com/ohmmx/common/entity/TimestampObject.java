package com.ohmmx.common.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@MappedSuperclass
public abstract class TimestampObject<K extends Serializable> extends AbstractEntity<K> {

	public static final String COLUMN_CREATE_TIMESTAMP = "CREATE_TIMESTAMP";
	public static final String COLUMN_MODIFY_TIMESTAMP = "MODIFY_TIMESTAMP";

	/**
	 * 创建时间戳.
	 *
	 * CREATE_TIMESTAMP
	 */
	protected Date createTimestamp;

	/**
	 * 修改时间戳.
	 *
	 * MODIFY_TIMESTAMP
	 */
	protected Date modifyTimestamp;

	/**
	 * 创建时间戳.
	 *
	 * CREATE_TIMESTAMP
	 *
	 * @return the createTimestamp
	 */
	@Column(name = COLUMN_CREATE_TIMESTAMP, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreateTimestamp() {
		return createTimestamp;
	}

	/**
	 * 创建时间戳.
	 *
	 * CREATE_TIMESTAMP
	 *
	 * @param createTimestamp the createTimestamp to set
	 */
	public void setCreateTimestamp(final Date createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	/**
	 * 修改时间戳.
	 *
	 * MODIFY_TIMESTAMP
	 *
	 * @return the modifyTimestamp
	 */
	@Column(name = COLUMN_MODIFY_TIMESTAMP)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getModifyTimestamp() {
		return modifyTimestamp;
	}

	/**
	 * 修改时间戳.
	 *
	 * MODIFY_TIMESTAMP
	 *
	 * @param modifyTimestamp the modifyTimestamp to set
	 */
	public void setModifyTimestamp(final Date modifyTimestamp) {
		this.modifyTimestamp = modifyTimestamp;
	}
}
