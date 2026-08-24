package org.example.bai2.config;




import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(LangfuseProperties.class)
public class LangfuseConfig {

    @Bean
    public LangfuseClient langfuseClient(
            LangfuseProperties properties) {

        return new LangfuseClient(
                properties.getPublicKey(),
                properties.getSecretKey(),
                properties.getBaseUrl()
        );
    }
}
