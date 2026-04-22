package ru.omstu.fitprogwork.db;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "ru.omstu.fitprogwork.db")
public class JpaConfig {

    @Bean
    public DataSource dataSource(
            @Value("${db.url}") String url,
            @Value("${db.username}") String user,
            @Value("${db.password}") String password
    ) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(url, user, password);
        dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        EclipseLinkJpaVendorAdapter jpaVendorAdapter = new EclipseLinkJpaVendorAdapter();
        jpaVendorAdapter.setDatabase(Database.HSQL);
        jpaVendorAdapter.setGenerateDdl(true);
        jpaVendorAdapter.setShowSql(true);

        Map<String, Object> properties = new HashMap<>();
        properties.put("eclipselink.weaving", "false");

        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter);
        entityManagerFactory.setPackagesToScan("ru.omstu.fitprogwork.db");
        entityManagerFactory.setJpaPropertyMap(properties);
        return entityManagerFactory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
