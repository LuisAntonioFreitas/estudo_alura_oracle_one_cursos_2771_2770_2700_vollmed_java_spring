package net.lanet.vollmed.infra.springdocswagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import net.lanet.vollmed.infra.utilities.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocSwaggerConfigurations {

    @Autowired
    private ApplicationProperties ap;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // Token
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                // Others
                .info(new Info()
                        .title(ap.apiSystemTagBase)
                        .description(ap.apiSystemName)
                        .version(ap.apiSystemVersion)
                );
    }

}
