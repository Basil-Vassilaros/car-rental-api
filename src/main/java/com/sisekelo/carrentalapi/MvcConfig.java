package com.sisekelo.carrentalapi;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    public void addViewController(ViewControllerRegistry registry){
        registry.addViewController("/index").setViewName("home");
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/cars").setViewName("cars");

    }
}
