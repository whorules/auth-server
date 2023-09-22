package com.korovko.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.security")
@Getter
@Setter
public class ApplicationSecurityProperties { // todo make record instead

    private String jwtSecret;
    private int jwtTimeToLive;

}
