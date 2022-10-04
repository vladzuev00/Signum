package by.aurorasoft.signum.config;

import by.aurorasoft.signum.config.property.DataSourceProperty;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DBConfiguration {

    @Autowired
    @Bean
    public DataSource dataSource(DataSourceProperty dataSourceProperty) {
        final HikariConfig hikariConfig = new HikariConfig();
//        hikariConfig.setDriverClassName(dataSourceProperty.getDriverClassName());
//        hikariConfig.setJdbcUrl(dataSourceProperty.getUrl());
//        hikariConfig.setUsername(dataSourceProperty.getUsername());
//        hikariConfig.setPassword(dataSourceProperty.getPassword());
        hikariConfig.setDriverClassName("org.postgresql.Driver");
        hikariConfig.setJdbcUrl("jdbc:postgresql://localhost:5432/signum");
        hikariConfig.setUsername("postgres");
        hikariConfig.setPassword("root");
        return new HikariDataSource(hikariConfig);
    }
}
