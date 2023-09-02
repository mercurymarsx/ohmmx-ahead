package com.ohmmx.common.entity;

import java.io.Serializable;

public interface BaseEntity<K extends Serializable> extends Serializable {

	public K getId();

	public void setId(K id);

	@Override
	public String toString();

	@Override
	public int hashCode();

	@Override
	public boolean equals(Object target);
}
