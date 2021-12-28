package com.song.project.armeria.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "song.armeria")
public class ArmeriaProperty {
    private int httpPort;
    private int httpsPort;

    ArmeriaProperty(){

    }
}
