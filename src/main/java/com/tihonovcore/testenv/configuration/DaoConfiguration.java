package com.tihonovcore.testenv.configuration;

import com.tihonovcore.testenv.dao.InMemoryDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoConfiguration {
    @Bean
    public InMemoryDao getDao() {
        return new InMemoryDao();
    }
}
