package ch.fhnw.shakethelakebackend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

/**
 * Swagger configuration
 */
@OpenAPIDefinition(
    info = @Info(title = "Shake The Lake Backend", version = "1.0"),
    security = @SecurityRequirement(name = "firebaseAuth")
)
@SecurityScheme(
    name = "firebaseAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "Provide Firebase ID Token as Bearer Token"
)
public class SwaggerConfig {

}
