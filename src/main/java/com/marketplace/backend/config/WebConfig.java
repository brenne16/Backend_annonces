package com.marketplace.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/api/files/**")
                .addResourceLocations(
                        "file:///C:/Users/Hello/Documents/Ma_version_projet_tutore/annonces-platform/backend/backend/uploads/"
                );
    }
}