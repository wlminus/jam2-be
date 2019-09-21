package com.wlminus.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import org.hibernate.cache.jcache.ConfigSettings;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.wlminus.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.wlminus.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.wlminus.domain.User.class.getName());
            createCache(cm, com.wlminus.domain.Authority.class.getName());
            createCache(cm, com.wlminus.domain.User.class.getName() + ".authorities");
            createCache(cm, com.wlminus.domain.AppConst.class.getName());
            createCache(cm, com.wlminus.domain.Category.class.getName());
            createCache(cm, com.wlminus.domain.Customer.class.getName());
            createCache(cm, com.wlminus.domain.Customer.class.getName() + ".shopOrders");
            createCache(cm, com.wlminus.domain.Media.class.getName());
            createCache(cm, com.wlminus.domain.Media.class.getName() + ".products");
            createCache(cm, com.wlminus.domain.ShopNew.class.getName());
            createCache(cm, com.wlminus.domain.ShopNew.class.getName() + ".tags");
            createCache(cm, com.wlminus.domain.Product.class.getName());
            createCache(cm, com.wlminus.domain.Product.class.getName() + ".media");
            createCache(cm, com.wlminus.domain.Product.class.getName() + ".productSizes");
            createCache(cm, com.wlminus.domain.Product.class.getName() + ".tags");
            createCache(cm, com.wlminus.domain.ProductSize.class.getName());
            createCache(cm, com.wlminus.domain.ProductSize.class.getName() + ".products");
            createCache(cm, com.wlminus.domain.ShopOrder.class.getName());
            createCache(cm, com.wlminus.domain.ShopOrder.class.getName() + ".orderDescs");
            createCache(cm, com.wlminus.domain.OrderDesc.class.getName());
            createCache(cm, com.wlminus.domain.Tag.class.getName());
            createCache(cm, com.wlminus.domain.Tag.class.getName() + ".products");
            createCache(cm, com.wlminus.domain.Tag.class.getName() + ".news");
            createCache(cm, com.wlminus.domain.Province.class.getName());
            createCache(cm, com.wlminus.domain.Province.class.getName() + ".districts");
            createCache(cm, com.wlminus.domain.Province.class.getName() + ".customers");
            createCache(cm, com.wlminus.domain.Province.class.getName() + ".shopOrders");
            createCache(cm, com.wlminus.domain.District.class.getName());
            createCache(cm, com.wlminus.domain.District.class.getName() + ".wards");
            createCache(cm, com.wlminus.domain.District.class.getName() + ".customers");
            createCache(cm, com.wlminus.domain.District.class.getName() + ".shopOrders");
            createCache(cm, com.wlminus.domain.Ward.class.getName());
            createCache(cm, com.wlminus.domain.Ward.class.getName() + ".customers");
            createCache(cm, com.wlminus.domain.Ward.class.getName() + ".shopOrders");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cm.destroyCache(cacheName);
        }
        cm.createCache(cacheName, jcacheConfiguration);
    }
}
