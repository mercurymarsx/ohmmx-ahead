package com.ohmmx.common.id;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import jakarta.annotation.PostConstruct;

@Configuration
@Lazy(false)
public class InitStaticPropertyValue {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private YamlProperties properties;

	/**
	 * 初始化 工具类的一些参数值
	 */
	@PostConstruct
	public void init() {
		this.setStaticProperty(IdGenerator.class, "datacenter", properties.getDatacenter());
		this.setStaticProperty(IdGenerator.class, "worker", properties.getWorker());
	}

	/**
	 * 设置类的静态属性
	 *
	 * @param T
	 * @param fieldName
	 * @param object
	 */
	private void setStaticProperty(Class<?> T, String fieldName, Object value) {
		try {
			Field field = T.getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(null, value);
		} catch (Exception e) {
			logger.error("设置类的静态属性错误", e);
		}
	}
}
