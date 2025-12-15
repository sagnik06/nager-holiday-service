package com.example.nager.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;
@Configuration
@EnableCaching
public class CacheConfig {
    @Value("#{'${spring.cache.cache-names}'.split(',')}")
    private List<String> cacheNames;
    @Value("${app.cache.async:true}")
    private boolean async;
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cm = new CaffeineCacheManager();
        cm.setCacheNames(cacheNames);
        cm.setAsyncCacheMode(async);
        return cm;
    }
}
