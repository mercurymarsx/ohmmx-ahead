package com.ohmmx.common.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "CFG_PARAMETER")
public class ConfigParameter extends TimestampObject<String> {

	private String id;

	@Id
	@Column(name = "ID", length = 64)
	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	private boolean reloadable;

	private String stringValue;

	private int intValue;

	private BigDecimal decimalValue;

	private long longValue;

	private boolean booleanValue;

	@Column(name = "RELOADABLE")
	public boolean isReloadable() {
		return reloadable;
	}

	public void setReloadable(boolean reloadable) {
		this.reloadable = reloadable;
	}

	@Column(name = "STRING_VALUE", length = 255)
	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	@Column(name = "INT_VALUE", precision = 11)
	public int getIntValue() {
		return intValue;
	}

	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}

	@Column(name = "DECIMAL_VALUE", precision = 19, scale = 2)
	public BigDecimal getDecimalValue() {
		return decimalValue;
	}

	public void setDecimalValue(BigDecimal decimalValue) {
		this.decimalValue = decimalValue;
	}

	@Column(name = "LONG_VALUE", precision = 19)
	public long getLongValue() {
		return longValue;
	}

	public void setLongValue(long longValue) {
		this.longValue = longValue;
	}

	@Column(name = "BOOLEAN_VALUE")
	public boolean isBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(boolean booleanValue) {
		this.booleanValue = booleanValue;
	}
}
