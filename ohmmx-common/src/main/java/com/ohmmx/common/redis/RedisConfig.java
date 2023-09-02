package com.ohmmx.common.redis;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;

@Configuration
@EnableCaching
public class RedisConfig {

	@Value("${spring.data.redis.host:localhost}")
	private String host;

	@Value("${spring.data.redis.database:0}")
	private int database;

	@Value("${spring.data.redis.port:6379}")
	private int port;

	@Value("${spring.data.redis.password:}")
	private String password;

	@Value("${spring.data.redis.ssl.enabled:false}")
	private boolean sslEnabled;

	@Value("${spring.data.redis.timeout:300000}")
	private long timeout;

	@Value("${spring.data.redis.lettuce.shutdown-timeout:500}")
	private long shutdownTimeout;

	@Bean
	public LettuceConnectionFactory lettuceConnectionFactory() {
		RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
		configuration.setHostName(host);
		configuration.setDatabase(database);
		configuration.setPort(port);
		configuration.setPassword(password);

		LettucePoolingClientConfigurationBuilder builder = LettucePoolingClientConfiguration.builder();
		if (sslEnabled) {
			builder.useSsl();
		}
		builder //
				// .poolConfig(genericObjectPoolConfig) //
				.commandTimeout(Duration.ofMillis(timeout)) //
				.shutdownTimeout(Duration.ofMillis(shutdownTimeout));
		LettuceClientConfiguration client = builder.build();

		LettuceConnectionFactory factory = new LettuceConnectionFactory(configuration, client);
		// factory.setShareNativeConnection(true);
		// factory.setValidateConnection(false);

		return factory;
	}

	@Bean
	public RedisTemplate<String, String> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
		RedisTemplate<String, String> template = new RedisTemplate<>();
		template.setConnectionFactory(lettuceConnectionFactory);

		// 使用Jackson2JsonRedisSerializer替换默认的JdkSerializationRedisSerializer来序列化和反序列化redis的value值
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
		Jackson2JsonRedisSerializer<String> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(mapper, String.class);

		StringRedisSerializer stringRedisSerializer = StringRedisSerializer.ISO_8859_1;

		// key采用String序列化方式
		template.setKeySerializer(stringRedisSerializer);
		// value采用jackson序列化方式
		template.setValueSerializer(jackson2JsonRedisSerializer);
		// hash-key采用String序列化方式
		template.setHashKeySerializer(stringRedisSerializer);
		// hash-value采用jackson序列化方式
		template.setHashValueSerializer(jackson2JsonRedisSerializer);
		template.afterPropertiesSet();
		return template;
	}
}
