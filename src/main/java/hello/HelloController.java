package hello;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    @RequestMapping("/")
    public String index() {
        String ipAddress = "Unknown IP Address";
        
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            ipAddress = inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            // Replaced e.printStackTrace() with a logger
            logger.error("Could not determine local host address", e);
        }

        return "JAVA application deployed on DOCKER CONTAINER 12-Jan... By Vignan. IP Address: " + ipAddress;
    }
}