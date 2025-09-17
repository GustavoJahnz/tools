package challenge.tools.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI apiJavaToolsChallenge() {
        return new OpenAPI()
                .info(new Info()
                        .title("API para Java Tools Challenge")
                        .description("Operações de Pagamento, Estorno e Consulta")
                        .version("v1.0"));
    }
}
