package br.com.denuncia.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DenuniaConfig {

    @Value("${openai.api-key}")
    private String apikey;

    @Bean
    protected RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Authorization", "Bearer " + apikey);
            requestTemplate.header("Content-Type", "application/json");
        };
    }


}
