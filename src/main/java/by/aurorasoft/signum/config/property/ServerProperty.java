package by.aurorasoft.signum.config.property;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "netty.server")
@NoArgsConstructor
@Setter
@Getter
public class ServerProperty {
    private int port;
    private int connectionThreads;
    private int dataProcessThreads;
    private int timeoutSeconds;
}
