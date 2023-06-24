package com.ohmmx.steam.shop.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "添加APP", description = "需要手动添加的应用")
public class StoreAppDto {
	@Schema(name = "APP列表")
	private List<Integer> apps;

	@Schema(name = "DLC列表")
	private List<Integer> dlcs;

	public List<Integer> getApps() {
		return apps;
	}

	public void setApps(List<Integer> apps) {
		this.apps = apps;
	}

	public List<Integer> getDlcs() {
		return dlcs;
	}

	public void setDlcs(List<Integer> dlcs) {
		this.dlcs = dlcs;
	}
}
