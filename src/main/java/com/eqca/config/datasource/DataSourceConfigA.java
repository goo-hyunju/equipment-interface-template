package com.eqca.config.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * 데이터베이스 커넥션 및 통신
 * @since 1.0
 * @version 2.0
 * @author x1
 */
@Configuration
@EnableTransactionManagement
public class DataSourceConfigA {

    /**
     * 데이터베이스 연결 풀 설정
     */
    @Bean("hikariConfigA")
    @ConfigurationProperties(prefix = "spring.datasource.a")
    HikariConfig hikariConfigA() {
        return new HikariConfig();
    }

    @Bean("dataSourceA")
    HikariDataSource dataSourceA() {
        return new HikariDataSource(hikariConfigA());
    }

    @Primary
    @Bean("transactionManagerA")
    @Qualifier("transactionManagerA")
    PlatformTransactionManager transactionManagerA(@Qualifier("dataSourceA") DataSource dataSourceA){
        return new DataSourceTransactionManager(dataSourceA);
    }
}
