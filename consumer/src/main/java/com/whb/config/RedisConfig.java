package com.whb.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.internal.util.Lists;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
@EnableCaching
public class RedisConfig {

    /**
     * 管理缓存
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory){
        //通过Spring提供的RedisCacheConfiguration类，构造一个自己的redis配置类，从该配置类中可以设置一些初始化的命名空间
        //以及默认的对应过期时间等属性，再利用RedisCacheManager中的builder.build()的方式生成cacheManager
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();//生成一个默认的配置，通过config对象即可对缓存进行配置
        config = config.entryTtl(Duration.ofMillis(1))//设置缓存默认的过期时间
        .disableCachingNullValues();

        //设置一个初始化的缓存空间set集合
        Set<String> cacheName = new HashSet<>(16);
        cacheName.add("my-redis-cache1");
        cacheName.add("my-redis-cache2");

        //对每个命名空间使用不同的配置
        Map<String,RedisCacheConfiguration> configMap = new HashMap<>(16);
        configMap.put("my-redis-cache1",config);
        configMap.put("my-redis-cache2",config.entryTtl(Duration.ofMillis(120)));

        RedisCacheManager manager = RedisCacheManager.builder(redisConnectionFactory)
                .initialCacheNames(cacheName)
                .withInitialCacheConfigurations(configMap)
                .build();
        return manager;
    }

    /**
     * 操作redis模板
     * @param factory
     * @return
     */
    @Bean
    public RedisTemplate<Object ,Object> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<Object,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        serializer.setObjectMapper(objectMapper);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

}
