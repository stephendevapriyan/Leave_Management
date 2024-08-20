package com.example.Multipledatabse.secondary;






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
        entityManagerFactoryRef = "databaseEntity",
        transactionManagerRef = "transaction"

)

public class Configurationsecondary {

    @Bean
    LocalContainerEntityManagerFactoryBean databaseEntity( EntityManagerFactoryBuilder entitymanagerFactorybuilder, @Qualifier("Secondary") DataSource ddataSource){

        return entitymanagerFactorybuilder
                .dataSource(ddataSource)
                .packages("com.example.Multipledatabse.table2")
                .build();
    }
    @Bean
    PlatformTransactionManager transaction(@Qualifier("databaseEntity") LocalContainerEntityManagerFactoryBean em){

        return new JpaTransactionManager(em.getObject());
    }
}
