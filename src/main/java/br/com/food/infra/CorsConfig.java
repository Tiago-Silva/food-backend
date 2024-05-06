package br.com.food.infra;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*") // permite requisições de qualquer origem
                .allowedMethods("*") // permite qualquer método (GET, POST, PUT, etc.)
                .allowedHeaders("*") // permite qualquer header
                .allowCredentials(true)
                .maxAge(3600); // tempo de vida do CORS preflight
    }
}