package com.ohmmx.common.entity;

import java.io.Serializable;

public abstract class AbstractEntity<K extends Serializable> implements BaseEntity<K> {

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
		return super.toString();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		// return HashCodeBuilder.reflectionHashCode(this, true);
		return super.hashCode();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object target) {
		// return EqualsBuilder.reflectionEquals(this, target, true);
		return super.equals(target);
	}
}
