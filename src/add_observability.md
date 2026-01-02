Spring Boot Observability Guide: Metrics & Logging

This guide provides a foundational setup for achieving robust observability in a Java Spring Boot backend application, focusing on standard, modern practices for collecting metrics (via Micrometer/Prometheus) and structured logs (via SLF4J/Logback).

📈 Collecting Metrics: Micrometer & Prometheus

Micrometer acts as an instrumentation facade, allowing you to use a consistent API while outputting metrics to various monitoring systems. We use the Prometheus registry because it's the industry standard for time-series data collection.

1. Add Dependencies (Maven Example)

Include the Spring Boot Actuator (which provides the necessary endpoints) and the Micrometer registry for Prometheus in your pom.xml.

<dependencies>
    <!-- Standard dependency for a web application -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- 1. Spring Boot Actuator (Enables /actuator endpoints) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>

    <!-- 2. Micrometer Registry for Prometheus (Exposes metrics in Prometheus format) -->
    <dependency>
        <groupId>io.micrometer</groupId>
        <artifactId>micrometer-registry-prometheus</artifactId>
    </dependency>
</dependencies>


2. Configure Endpoints

Enable the Prometheus endpoint in your application.properties so that a Prometheus server can scrape the metrics data from your application at the path /actuator/prometheus.

# application.properties

# Expose all actuator endpoints (including 'health' and 'metrics')
management.endpoints.web.exposure.include=* # Explicitly enable and expose the Prometheus endpoint
management.endpoint.prometheus.enabled=true 


3. Custom Metrics Instrumentation

Spring Boot automatically provides system metrics (JVM, Tomcat, HTTP), but you need to instrument your core business logic manually.

You inject the MeterRegistry and use it to register specific meter types, like Counter for counting events.

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    // 1. Declare the Counter instance
    private final Counter orderCounter;

    // 2. Inject the registry and initialize the meter
    public OrderService(MeterRegistry registry) {
        this.orderCounter = Counter.builder("orders.processed.total") // Metric name
            .description("Total number of orders processed")
            .tag("service", "order_processor") // Static tag/dimension
            .register(registry);
    }

    // Assume 'Order' is a defined class
    public void processOrder(Object order) {
        // ... business logic ...
        
        // 3. Update the metric whenever the event occurs
        this.orderCounter.increment(); 
        
        // ... more business logic ...
    }
}


📜 Collecting Logs: SLF4J and Structured Logback

Spring Boot uses SLF4J as a facade over Logback. The goal is to move beyond simple plain text and produce structured logs (JSON format) for reliable aggregation and analysis in external systems (like ELK stack, Splunk, etc.).

1. Standard Logging

The basic setup for standard logging is already in place with spring-boot-starter-web.

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {
    
    // Declare the logger for this class
    private static final Logger log = LoggerFactory.getLogger(MyController.class);

    @GetMapping("/hello")
    public String getHello() {
        // Basic informational logging
        log.info("Request received for /hello endpoint.");
        
        try {
            // ... critical business logic ...
        } catch (Exception e) {
            // **Crucial for aggregation:** Always log the exception object
            log.error("An unexpected error occurred during processing.", e);
        }
        return "Hello!";
    }
}


2. Structured Logging (JSON Format)

To output logs as JSON, you need a Logback encoder dependency and a custom Logback configuration file.

A. Add Dependency (Logstash Logback Encoder)

<!-- Maven pom.xml -->
<dependency>
    <groupId>net.logstash.logback</groupId>
    <artifactId>logstash-logback-encoder</artifactId>
    <version>7.4</version> <!-- Use a recent stable version -->
</dependency>


B. Logback Configuration (logback-spring.xml)

Create this file in your src/main/resources folder to override the default console output with a JSON encoder.

<!-- src/main/resources/logback-spring.xml -->
<configuration>
    <!-- Define an appender that uses the LogstashEncoder to output JSON -->
    <appender name="JSON_CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="net.logstash.logback.encoder.LogstashEncoder"/>
    </appender>

    <!-- Set the root logging level and reference the new JSON appender -->
    <root level="INFO">
        <appender-ref ref="JSON_CONSOLE"/>
    </root>
</configuration>


This configuration ensures your application's console output is a stream of JSON objects, ready to be collected by log shippers like Filebeat or Fluentd and sent to your aggregation platform.