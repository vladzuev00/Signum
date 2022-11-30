package by.aurorasoft.signum.config.property;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "message.validation")
@NoArgsConstructor
@Setter
@Getter
public class MessageValidationProperty {
    private int minValidAmountSatellite;
    private int maxValidAmountSatellite;
    private int minValidDOP;
    private int maxValidDOP;
}
