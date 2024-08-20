package com.example.Multipledatabse.primary;


import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
@EnableTransactionManagement
@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.Multipledatabse.repository",
        entityManagerFactoryRef = "databaseEntityManagerFactoryBean",
        transactionManagerRef = "transactionManagerr"

)
public class Configurationprimary {

    @Bean
    LocalContainerEntityManagerFactoryBean databaseEntityManagerFactoryBean(EntityManagerFactoryBuilder entitymanage, @Qualifier("primary") DataSource dataSource){

        return entitymanage
                .dataSource(dataSource)
                .packages("com.example.Multipledatabse.table")
                .build();
    }
    @Bean
    PlatformTransactionManager transactionManagerr(@Qualifier("databaseEntityManagerFactoryBean") LocalContainerEntityManagerFactoryBean lem){

        return new JpaTransactionManager(lem.getObject());
    }
}
