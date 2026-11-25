package com.riyaindustries.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${app.upload-dir:./uploads}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Expose ONLY public catalog images (services, machinery, projects, products).
        // Customer quotation drawings are stored in uploads/drawings/ and served only via authenticated Admin download.
        Path publicUploads = Paths.get(uploadDir, "public").toAbsolutePath().normalize();
        String location = publicUploads.toUri().toString();
        registry.addResourceHandler("/uploads/public/**")
            .addResourceLocations(location)
            .setCachePeriod(86400);
    }
}
