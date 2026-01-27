package sk.upjs.paz.testcontainer;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

public abstract class TestContainer {

    protected static PostgreSQLContainer<?> postgres;
    protected static JdbcTemplate jdbcTemplate;

    @BeforeAll
    static void startContainer() {
        postgres = new PostgreSQLContainer<>("postgres:15")
                .withDatabaseName("testdb")
                .withUsername("test")
                .withPassword("test");

        postgres.start();

        DataSource dataSource = new DriverManagerDataSource(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );

        jdbcTemplate = new JdbcTemplate(dataSource);

        initSchema();
    }

    private static void initSchema() {
        jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS cn");
    }

    @AfterAll
    static void stopContainer() {
        postgres.stop();
    }
}
