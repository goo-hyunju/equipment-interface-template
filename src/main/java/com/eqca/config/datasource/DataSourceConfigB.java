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
public class DataSourceConfigB {

    /**
     * 데이터베이스 연결 풀 설정
     */
    @Bean("hikariConfigB")
    @ConfigurationProperties(prefix = "spring.datasource.b")
    HikariConfig hikariConfigB() {
        return new HikariConfig();
    }

    @Bean("dataSourceB")
    HikariDataSource dataSourceB() {
        return new HikariDataSource(hikariConfigB());
    }

    @Primary
    @Bean("transactionManagerB")
    @Qualifier("transactionManagerB")
    PlatformTransactionManager transactionManagerB(@Qualifier("dataSourceB") DataSource dataSourceB){
        return new DataSourceTransactionManager(dataSourceB);
    }
}
