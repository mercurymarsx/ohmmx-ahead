package com.ohmmx.common.id;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class YamlProperties {
	@Value("${server.cluster.datacenter:1}")
	private long datacenter;
	@Value("${server.cluster.worker:1}")
	private long worker;

	public long getDatacenter() {
		return datacenter;
	}

	public void setDatacenter(long datacenter) {
		this.datacenter = datacenter;
	}

	public long getWorker() {
		return worker;
	}

	public void setWorker(long worker) {
		this.worker = worker;
	}
}
