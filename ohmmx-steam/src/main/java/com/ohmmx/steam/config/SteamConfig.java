package com.ohmmx.steam.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "steam")
@Component
public class SteamConfig {

	public SteamConfig() {
	}

	private String apikey;
	private String id64;
	private SteamProxy proxy;

	public String getApikey() {
		return apikey;
	}

	public void setApikey(String apikey) {
		this.apikey = apikey;
	}

	public String getId64() {
		return id64;
	}

	public void setId64(String id64) {
		this.id64 = id64;
	}

	public SteamProxy getProxy() {
		return proxy;
	}

	public void setProxy(SteamProxy proxy) {
		this.proxy = proxy;
	}
}
