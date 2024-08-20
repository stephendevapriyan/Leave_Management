package com.example.Multipledatabse.secondary;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;


import javax.sql.DataSource;

@Configuration
public class DataSourcesecondary {

    @ConfigurationProperties("spring.datasource.secondary")
    @Bean
    public DataSourceProperties postgre(){

        return new DataSourceProperties();
    }
    @Bean
    public DataSource Secondary(){

    DriverManagerDataSource datasoures =new DriverManagerDataSource();

    datasoures.setUsername(postgre().getName());
    datasoures.setPassword(postgre().getPassword());
    datasoures.setUrl(postgre().getUrl());
    datasoures.setDriverClassName(postgre().getDriverClassName());

        return postgre().initializeDataSourceBuilder().build();
    }
}
