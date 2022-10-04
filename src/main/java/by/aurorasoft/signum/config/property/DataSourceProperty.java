package by.aurorasoft.signum.config.property;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "db.datasource")
@NoArgsConstructor
@Setter
@Getter
public class DataSourceProperty {
    private String url;
    private String username;
    private String password;
    private String driverClassName;
}
