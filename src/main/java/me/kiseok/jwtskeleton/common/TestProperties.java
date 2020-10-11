package me.kiseok.jwtskeleton.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter @Setter
@ConfigurationProperties("test")
@Component
public class TestProperties {
    private String email;
    private String password;
    private String name;
    private String picture;
}
