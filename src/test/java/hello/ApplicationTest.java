package hello;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTest {

    // 1. Code Smell: Unused private field
    private String unusedField = "I am a code smell";

    @Test
    void contextLoads() {
        // This ensures the application context starts without errors
    }

    @Test
    void main() {
        Application.main(new String[] {});
    }

    /**
     * SECURITY HOTSPOT & VULNERABILITY DEMO
     * This method contains multiple issues for SonarQube to flag.
     */
    @Test
    void securityVulnerabilityDemo() {
        // 2. Security Hotspot: Use of hardcoded IP address
        String ipAddress = "192.168.1.1"; 

        // 3. Vulnerability: Hardcoded Credentials (CWE-798)
        String user = "admin";
        String pass = "password123"; 

        try {
            // 4. Security Hotspot: Database connection without encryption/validation
            // 5. Code Smell: Using DriverManager instead of a DataSource
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + ipAddress + "/db", user, pass);
            
            Statement stmt = conn.createStatement();
            
            // 6. Critical Vulnerability: SQL Injection (CWE-89)
            // Concatenating input directly into a query string
            String query = "SELECT * FROM users WHERE name = '" + user + "'";
            ResultSet rs = stmt.executeQuery(query);

            // 7. Bug: Potential NullPointerException
            // Not checking if rs is null before access
            while (rs.next()) {
                // 8. Maintainability: System.out used instead of a Logger
                System.out.println(rs.getString(1));
            }

        } catch (Exception e) {
            // 9. Code Smell: Generic Exceptions and "PrintStackTrace" 
            // Better to catch specific exceptions and use a logger.
            e.printStackTrace();
        }
    }

    // 10. Reliability: Empty Method / Cognitive Complexity
    @Test
    void uselessTest() {
        // An empty test that does nothing is often flagged as a code smell
    }
}