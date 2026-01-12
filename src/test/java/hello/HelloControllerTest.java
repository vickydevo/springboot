package hello;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HelloControllerTest { // Removed 'public'

    @Autowired
    private HelloController controller;

    @Test
    void testIndex() { // Removed 'public'
        String result = controller.index();
        assertNotNull(result);
    }
}