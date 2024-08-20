package com.example.Multipledatabse.primary;


import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceprimary {

@ConfigurationProperties("spring.datasource.primary")
@Primary
@Bean
 public DataSourceProperties postgres(){

    return new DataSourceProperties();
}

@Bean
public DataSource primary(){

    DriverManagerDataSource datasoures =new DriverManagerDataSource();

    datasoures.setUsername(postgres().getName());
    datasoures.setPassword(postgres().getPassword());
    datasoures.setUrl(postgres().getUrl());
    datasoures.setDriverClassName(postgres().getDriverClassName());

    return postgres().initializeDataSourceBuilder().build();
}

}
