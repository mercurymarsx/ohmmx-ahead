package com.ohmmx.common.core.jmx;

import java.util.Map;

import com.ohmmx.common.entity.ConfigParameter;

public interface ConfigurableBean {

	void refreshConfig(Map<String, ConfigParameter> parameters);
}
