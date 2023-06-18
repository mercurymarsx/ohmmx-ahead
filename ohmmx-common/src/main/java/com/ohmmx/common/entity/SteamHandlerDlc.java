package com.ohmmx.common.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "STEAM_HANDLER_DLC")
public class SteamHandlerDlc {
	@Id
	private int appid;
	private boolean hasDlc;
	private boolean want;
	private int total;
	private int owned;
	private int alive;

	public SteamHandlerDlc() {
	}

	public int getAppid() {
		return appid;
	}

	public void setAppid(int appid) {
		this.appid = appid;
	}

	public boolean isHasDlc() {
		return hasDlc;
	}

	public void setHasDlc(boolean hasDlc) {
		this.hasDlc = hasDlc;
	}

	public boolean isWant() {
		return want;
	}

	public void setWant(boolean want) {
		this.want = want;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getOwned() {
		return owned;
	}

	public void setOwned(int owned) {
		this.owned = owned;
	}

	public int getAlive() {
		return alive;
	}

	public void setAlive(int alive) {
		this.alive = alive;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
