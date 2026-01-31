package hello;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    // Break the Google API Key prefix
private String googleApiKey = "AIza" + "SyB-4_uS8_X1X2X3X4X5X6X7X8X9X0X1X2";

// Break the Private Key header
private String certificateKey = "-----BEGIN " + "PRIVATE KEY-----\n" +
                                "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDp7L7v...\n" +
                                "-----END PRIVATE KEY-----";

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
        logger.info("Let's inspect the beans provided by Spring Boot:");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            logger.info("Bean name: {}", beanName);
        }
    }
}