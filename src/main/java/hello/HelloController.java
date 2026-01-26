package hello;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class HelloController {

    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    @RequestMapping("/")
    public String index(HttpServletRequest request) {
        String serverIp = "Unknown Server IP";
        try {
            serverIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            logger.error("Could not determine server host address", e);
        }

        String clientIp = request.getRemoteAddr();
        logger.info("Incoming request | Client IP: {} | Target: /", clientIp);

    
        return "JAVA application deployed on DOCKER CONTAINER By Vignan. " +
       "Server IP: " + serverIp + " | Request from Client IP: " + clientIp;
    }

    // --- SONARQUBE TEST BLOCK START ---

    /**
     * TRIGGER: Security Hotspot & Hardcoded Password
     */
    @RequestMapping("/security")
    public void securityIssues() {
        String databaseIp = "192.168.1.100"; // Hotspot
        String adminPassword = "password123"; // Vulnerability
        logger.info("Connecting to {} with secret: {}", databaseIp, adminPassword);
    }

    /**
     * TRIGGER: Reliability (Bug) - Null Pointer Risk
     */
    @RequestMapping("/bug")
    public String triggerBug(String input) {
        String test = null;
        // This will throw NullPointerException if input is not "test"
        if (input.equals("test")) { 
            return test.toUpperCase(); 
        }
        return "No bug hit yet";
    }

    /**
     * TRIGGER: Maintainability (Code Smells)
     */
    @RequestMapping("/smell")
    public String triggerSmell(int a, int b, int c) {
        // High Cognitive Complexity (Deep nesting)
        if (a > 0) {
            if (b > 0) {
                if (c > 0) {
                    return "Very complex logic!";
                }
            }
        }
        
        int unusedVariable = 100; // Unused variable smell

        try {
            Thread.sleep(100);
        } catch (Exception e) {
            // Empty catch block smell
        }

        return "Smells are present";
    }
    // --- SONARQUBE TEST BLOCK END ---
}