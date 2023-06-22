package com.ohmmx.common.entity;

import org.hibernate.annotations.GenericGenerator;

import com.ohmmx.common.id.IdGenerator;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity {
	@Id
	@GenericGenerator(name = "idGen", type = IdGenerator.class)
	@GeneratedValue(generator = "idGen")
	protected String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
