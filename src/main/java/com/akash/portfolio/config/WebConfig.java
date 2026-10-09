package com.akash.portfolio.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /*
     * Static Resource Configuration
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // CSS
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");

        // JavaScript
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");

        // Images
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");

        // Icons
        registry.addResourceHandler("/icons/**")
                .addResourceLocations("classpath:/static/icons/");

        // Videos
        registry.addResourceHandler("/videos/**")
                .addResourceLocations("classpath:/static/videos/");

        // Fonts
        registry.addResourceHandler("/fonts/**")
                .addResourceLocations("classpath:/static/fonts/");

        /*
         * Uploaded Files
         *
         * Example:
         * uploads/projects/
         * uploads/profile/
         * uploads/resume/
         * uploads/certificates/
         */

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }

    /*
     * Simple View Mapping
     */

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        registry.addViewController("/login")
                .setViewName("login");

        registry.addViewController("/404")
                .setViewName("404");

    }

}