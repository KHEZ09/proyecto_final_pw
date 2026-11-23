package com.example.demo.config;

import java.nio.charset.StandardCharsets;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class TriggerInitializer {

    // Los runners se ejecutan con el contexto y las tablas JPA preparados.
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    CommandLineRunner instalarTriggers(JdbcTemplate jdbc) {
        return ignoredArgs -> {
            var resource = new ClassPathResource("database/triggers.sql");
            var sql = resource.getContentAsString(StandardCharsets.UTF_8);
            // Se envia el script completo para respetar los bloques $$ de PL/pgSQL.
            jdbc.execute(sql);
        };
    }
}
