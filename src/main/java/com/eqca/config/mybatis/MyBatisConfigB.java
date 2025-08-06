package com.eqca.config.mybatis;


import org.apache.ibatis.logging.log4j2.Log4j2Impl;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;

/**
 * MyBatis 설정
 * @since 1.0
 * @version 2.0
 * @author x1
 */
@Configuration
@MapperScan(basePackages = "com.eqca.repository.mapper.b",
        sqlSessionFactoryRef = "sqlSessionFactoryB")
public class MyBatisConfigB {

    org.apache.ibatis.session.Configuration configuration () {
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();

        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setLogImpl(Log4j2Impl.class);

        return configuration;
    }

    @Bean("sqlSessionFactoryB")
    @Qualifier("sqlSessionFactoryB")
    SqlSessionFactory sqlSessionFactoryB(@Qualifier("dataSourceB") DataSource dataSourceB) throws Exception {

        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        // Configuration setting
        factoryBean.setConfiguration(configuration());

        factoryBean.setDataSource(dataSourceB);
        factoryBean.setVfs(SpringBootVFS.class);
        factoryBean.setTypeAliasesPackage("com.eqca.repository.dto.*");

        // Mapper location setting
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        factoryBean.setMapperLocations(resolver.getResources("classpath:mapper/b/*.xml"));

        return factoryBean.getObject();
    }
}
