package co.com.bancolombia.config;


import co.com.bancolombia.config.bd.secret.SecretConfig;
import co.com.bancolombia.model.commons.secretsmodel.SecretsModel;
import co.com.bancolombia.model.commons.secretsmodel.gateways.SecretsManagerConsumer;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "primaryEntityManagerFactory", transactionManagerRef = "primaryTransactionManager", basePackages = {
        "co.com.bancolombia.jpa" })
public class JpaConfig {

    @Primary
    @Bean(name = "primaryDataSourceProperties")
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties primaryDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = "primaryDataSource")
    @ConfigurationProperties("spring.datasource.configuration")
    public DataSource primaryDataSource(
            @Qualifier("primaryDataSourceProperties") DataSourceProperties primaryDataSourceProperties,
            SecretsManagerConsumer<SecretsModel> consumer, @Qualifier("secretConfig") SecretConfig secrectConfig) {

        SecretsModel model = secrectConfig.getModel(consumer);
String url = secrectConfig.getDbConnectionString(model);
        return primaryDataSourceProperties.initializeDataSourceBuilder()
                .url(secrectConfig.getDbConnectionString(model))
                .username(model.getUsername())
                .password(model.getPassword())
                .type(HikariDataSource.class).build();
    }

    @Primary
    @Bean(name = "primaryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(
            EntityManagerFactoryBuilder primaryEntityManagerFactoryBuilder,
            @Qualifier("primaryDataSource") DataSource primaryDataSource) {

        Map<String, String> primaryJpaProperties = new HashMap<>();

        primaryJpaProperties.put("hibernate.physical_naming_strategy", SpringPhysicalNamingStrategy.class.getName());
        primaryJpaProperties.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName());
        primaryJpaProperties.put("spring.jpa.hibernate.ddl-auto", "none");
        primaryJpaProperties.put("spring.jpa.database-platform", "org.hibernate.dialect.PostgreSQLDialect");

        return primaryEntityManagerFactoryBuilder.dataSource(primaryDataSource).packages("co.com.bancolombia.jpa")
                .persistenceUnit("primaryDataSource").properties(primaryJpaProperties).build();
    }

    @Primary
    @Bean(name = "primaryTransactionManager")
    public PlatformTransactionManager primaryTransactionManager(
            @Qualifier("primaryEntityManagerFactory") EntityManagerFactory primaryEntityManagerFactory) {

        return new JpaTransactionManager(primaryEntityManagerFactory);
    }
}
