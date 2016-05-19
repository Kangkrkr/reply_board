package com.pilot.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import com.pilot.model.UserModel;

@Configuration
@EnableCaching
public class RedisConfig {

	@Bean
	public JedisConnectionFactory jedisConnectionFactory(){
		// JRedis가 아니고 Jedis 였음..
		JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
		connectionFactory.setHostName("localhost");
		connectionFactory.setPort(6379);
		connectionFactory.setUsePool(true);
		
		return connectionFactory;
	}

	@Bean
	public RedisTemplate<?, ?> redisTemplate(){
		RedisTemplate<?, ?> redis = new RedisTemplate<Object, Object>();
		redis.setConnectionFactory(jedisConnectionFactory());
		redis.setKeySerializer(new GenericToStringSerializer<>(String.class));
		redis.setValueSerializer(new Jackson2JsonRedisSerializer<>(UserModel.class));
		
		return redis;
	}
	
	@Bean
	public StringRedisTemplate stringRedisTemplate(){
		StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
		stringRedisTemplate.setConnectionFactory(jedisConnectionFactory());
		return stringRedisTemplate;
	}
	
	@Bean
	public RedisCacheManager cacheManager(){
		RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate());
		
		return redisCacheManager;
	}
}
