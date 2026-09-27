package com.engine.core.testsupport;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.postgresql.PostgreSQLContainer;

public abstract class PostgresTestBase {

    @SuppressWarnings("resource")
    protected static final PostgreSQLContainer POSTGRES =
            new PostgreSQLContainer("postgres:17-alpine")
                    .withDatabaseName("hpg")
                    .withUsername("hpg")
                    .withPassword("hpg")
                    .withReuse(false);

    static {
        POSTGRES.start();
    }

    @DynamicPropertySource
    static void registerDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("jdbc.url", POSTGRES::getJdbcUrl);
        registry.add("jdbc.username", POSTGRES::getUsername);
        registry.add("jdbc.password", POSTGRES::getPassword);
    }
}