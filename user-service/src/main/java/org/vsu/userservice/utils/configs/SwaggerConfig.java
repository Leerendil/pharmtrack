package org.vsu.userservice.utils.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static org.vsu.userservice.utils.constants.UserServiceConstants.*;

@Configuration
public class SwaggerConfig {
    @Value("${server.port}")
    private String port;
    @Value("${app-mail}")
    private String appMail;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(SERVICE_TITLE)
                        .version(SERVICE_VERSION)
                        .description(SERVICE_DESCRIPTION)
                        .contact(new Contact()
                                .email(appMail)))
                .servers(List.of(
                        new Server().url(SERVER_URL + port).description(SERVER_DESCRIPTION)
                ));
    }
}
