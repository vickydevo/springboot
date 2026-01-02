package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@SpringBootApplication
public class Application {

    // Logger for application-level startup/shutdown/info messages
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        // Log application startup
        logger.info("Starting Spring Boot application");
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }

    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        // Filter that logs incoming HTTP requests (client info, query string, payload)
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeClientInfo(true); // include client IP and session id
        filter.setIncludeQueryString(true); // include URL query string
        filter.setIncludePayload(true); // include request body (careful with large payloads)
        filter.setMaxPayloadLength(10000); // limit payload size logged
        filter.setIncludeHeaders(false); // set true if headers should be logged
        return filter;
    }
}
