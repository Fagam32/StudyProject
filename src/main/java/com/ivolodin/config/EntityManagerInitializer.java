package com.ivolodin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Service
public class EntityManagerInitializer {
    public static final EntityManagerFactory factory = Persistence.createEntityManagerFactory("PU");
    private static EntityManager entityManager;
    @Bean
    public static EntityManager getEntityManager(){
        if (entityManager == null){
            entityManager = factory.createEntityManager();
        }
        return entityManager;
    }
}
