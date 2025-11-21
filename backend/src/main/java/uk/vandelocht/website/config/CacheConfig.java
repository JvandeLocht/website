package uk.vandelocht.website.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cache configuration for the application.
 * Enables caching for expensive operations like PDF generation.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("cvPdf");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                // Cache until application restart (no expiration)
                .maximumSize(1)
                .recordStats());
        return cacheManager;
    }
}
