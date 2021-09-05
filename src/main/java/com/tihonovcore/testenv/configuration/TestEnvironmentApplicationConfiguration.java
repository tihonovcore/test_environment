package com.tihonovcore.testenv.configuration;

import com.tihonovcore.testenv.dao.InMemoryDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class TestEnvironmentApplicationConfiguration {
    @Bean
    public InMemoryDao getDao() {
        return new InMemoryDao();
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/testenv");
        dataSource.setUsername("owner");
        dataSource.setPassword("123456");

        return dataSource;
    }
}
