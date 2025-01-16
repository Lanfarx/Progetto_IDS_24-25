package it.unicam.cs.filieraagricola.api.RestWebService;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class ProductServiceInterceptorAppConfig implements WebMvcConfigurer {
    public ProductServiceInterceptorAppConfig() {
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ProductServiceInterceptor());
    }
}
