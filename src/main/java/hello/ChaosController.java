package hello; // MUST match the folder 'hello'

import io.micrometer.core.annotation.Timed;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/chaos")
public class ChaosController {

    // Logger to emit application logs for this controller
    private static final Logger logger = LoggerFactory.getLogger(ChaosController.class);

    private static final List<byte[]> memoryLeakList = new ArrayList<>();

    @Timed(value = "chaos.errors", description = "Time taken to return errors")
    @GetMapping("/error")
    public String triggerError() {
        // Log that the endpoint was called (INFO level)
        logger.info("/chaos/error endpoint invoked - about to simulate failure");

        // Log an error with a simulated exception for structured/error monitoring
        logger.error("Simulating a runtime failure in triggerError");

        // Throwing an exception to simulate a failure (will also be logged by Spring and our global handler)
        throw new RuntimeException("Government Portal Service Failure - Simulated");
    }

    @Timed(value = "chaos.leak", description = "Time taken to allocate memory")
    @GetMapping("/leak")
    public String triggerLeak() {
        // DEBUG log showing intention to allocate memory (useful when investigating memory events)
        logger.debug("/chaos/leak called - allocating 20MB buffer. Current list size={}", memoryLeakList.size());

        // Allocate ~20MB and keep reference to simulate a memory leak
        byte[] data = new byte[20 * 1024 * 1024]; 
        memoryLeakList.add(data);

        // INFO-level log summarizing the new state after allocation
        logger.info("Memory allocated and added to list; new size={}", memoryLeakList.size());

        return "Memory Leaked! Current list size: " + memoryLeakList.size();
    }
}
