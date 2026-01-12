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
        // 1. Get the Server's internal IP (The Container)
        String serverIp = "Unknown Server IP";
        try {
            serverIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            logger.error("Could not determine server host address", e);
        }

        // 2. Get the Client IP (The source of the request)
        // This works for Laptop, Desktop, or Cloud VM
        String clientIp = request.getRemoteAddr();

        // 3. Professional Logging
        logger.info("Incoming request | Client IP: {} | Target: /", clientIp);

        return "JAVA application deployed on DOCKER CONTAINER 12-Jan... By Vignan. " +
               "Server IP: " + serverIp + " | Request from Client IP: " + clientIp;
    }
}