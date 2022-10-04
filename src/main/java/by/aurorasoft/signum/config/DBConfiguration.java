package by.aurorasoft.signum.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DBConfiguration {

    @Value("${db.datasource.url}")
    private String url;

    @Value("${db.datasource.username}")
    private String username;

    @Value("${db.datasource.password}")
    private String password;

    @Value("${db.datasource.driver-class-name}")
    private String driverClassName;

    @Bean
    public DataSource dataSource() {
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(this.url);
        hikariConfig.setUsername(this.username);
        hikariConfig.setPassword(this.password);
        hikariConfig.setDriverClassName(this.driverClassName);
        return new HikariDataSource(hikariConfig);
    }
}
