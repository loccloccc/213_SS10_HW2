package org.example.bai2.config;




import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "langfuse")
public class LangfuseProperties {

    private String publicKey;
    private String secretKey;
    private String baseUrl;
}
