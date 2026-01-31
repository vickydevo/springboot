package hello;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Value("${app.api.key:NOT_SET}")
    private String apiKey;

    @Value("${spring.datasource.password:NOT_SET}")
    private String dbPassword;

    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot! By Vignan";
    }

    @GetMapping("/config-check")
    public String checkConfig() {
        // We check if the secret exists, but we don't return the real value!
        boolean isApiKeyLoaded = !apiKey.equals("NOT_SET");
        return "API Key Loaded: " + isApiKeyLoaded;
    }
}