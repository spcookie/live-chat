package com.cqut.livechat.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Augenstern
 * @date 2022/6/12
 */
@Component
@ConfigurationProperties(prefix = "file")
@Setter
@Getter
public class FileProperties {
    private String dist;
}
