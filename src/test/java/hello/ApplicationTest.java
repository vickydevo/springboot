package hello;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTest {

    @Test
    void contextLoads() {
        // This ensures the application context starts without errors
    }

    @Test
    void main() {
        // This calls the main method to cover the SpringApplication.run line
        Application.main(new String[] {});
    }
}