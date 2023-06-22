package com.ohmmx.steam.config;

import java.net.InetSocketAddress;
import java.net.Proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import okhttp3.OkHttpClient;

@EnableConfigurationProperties(SteamConfig.class)
@Component
public class Configuration {
	@Autowired
	private SteamConfig steamConfig;

	public SteamConfig getSteamConfig() {
		return steamConfig;
	}

	public void requestWithProxy(OkHttpClient.Builder builder) {
		if (steamConfig.getProxy().isEnable()) {
			Proxy proxy;

			switch (steamConfig.getProxy().getProtocal()) {
			case "http":
				proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(steamConfig.getProxy().getHost(), steamConfig.getProxy().getPort()));
				break;
			case "socks4":
			case "socks5":
				proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(steamConfig.getProxy().getHost(), steamConfig.getProxy().getPort()));
				break;
			default:
				return;
			}
			builder.proxy(proxy).build();
		}
	}
}
