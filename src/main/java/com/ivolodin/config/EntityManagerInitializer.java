package com.ivolodin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Configuration
public class EntityManagerInitializer {
    public static final EntityManagerFactory factory = Persistence.createEntityManagerFactory("PU");

    @Bean
    public static EntityManager getEntityManager() {
        return factory.createEntityManager();
    }
}
