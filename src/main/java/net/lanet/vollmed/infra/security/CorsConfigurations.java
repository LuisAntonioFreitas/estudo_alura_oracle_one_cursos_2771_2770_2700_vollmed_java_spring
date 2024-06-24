package net.lanet.vollmed.infra.security;

import net.lanet.vollmed.infra.utilities.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
public class CorsConfigurations implements WebMvcConfigurer {

    @Autowired
    private ApplicationProperties ap;

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        List<String> listHeaders = List.of(ap.apiCorsHeaders.replace(" ","").split(","));
        List<String> listOrigins = List.of(ap.apiCorsOrigins.replace(" ","").split(","));
        List<String> listMethods = List.of(ap.apiCorsMethods.replace(" ","").split(","));

        registry.addMapping("/**")
                .allowedHeaders(listHeaders.toArray(new String[0]))
                .allowedOrigins(listOrigins.toArray(new String[0]))
                .allowedMethods(listMethods.toArray(new String[0]))
                .allowCredentials(true);
    }

}
