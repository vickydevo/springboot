package hello;

import java.net.InetAddress;
import java.net.UnknownHostException;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {

    // Logger for EFK (Elasticsearch, Fluentd, Kibana)
    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    // Counter for Prometheus
    private final Counter requestCounter;

    // Constructor Injection for MeterRegistry
    public HelloController(MeterRegistry registry) {
        this.requestCounter = registry.counter("vignan_app_requests_total", "type", "greeting");
    }

    @Timed(value = "vignan_index_latency", description = "Time taken to return greeting")
    @RequestMapping("/")
    public String index() {
        // 1. Increment Prometheus counter
        requestCounter.increment();

        // 2. Get IP Address
        String ipAddress = "Unknown";
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            ipAddress = inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            logger.error("Error retrieving IP address", e);
        }

        // 3. Log to Console (Scraped by Fluentd)
        logger.info("Request handled by pod with IP: {}", ipAddress);

        return "Greetings from 'vignan' deployed JAVA app in Minikube! Host private IPv4 Address: " + ipAddress;
    }
}
