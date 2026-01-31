package hello;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    // Gitleaks should catch this hardcoded secret below:
    private String leakedSecret = "v8N1S+pL9xR5qT2uW4yZ7A1B3C5D7E9F1G3H5I7J"; 

    @Value("${app.api.key:NOT_SET}")
    private String apiKey;

    @Value("${spring.datasource.password:NOT_SET}")
    private String 86d855dbPassword;

    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot! By Vignan";
    }

    @GetMapping("/config-check")
    public String checkConfig() {
        boolean isApiKeyLoaded = !apiKey.equals("NOT_SET");
        return "API Key Loaded: " + isApiKeyLoaded;
    }
}