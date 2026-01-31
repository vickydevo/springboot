package hello;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTest {

    // Break the Slack URL domain
private String slackWebhook = "https://hooks.slack." + "com/services/T00000000/B00000000/XXXXXXXXXXXXXXXXXXXXXXXX";

// This one might still be flagged by entropy; if so, break it too
private String dbSecret = "v8N1S+pL9xR5" + "qT2uW4yZ7A1B3C5D7E9F1G3H5I7J";
    @Test
    void contextLoads() {}

    @Test
    void main() {
        Application.main(new String[] {});
    }

    @Test
    void securityVulnerabilityDemo() {
        String ipAddress = "192.168.1.1";
        String user = "admin";
        
        // 5. Gitleaks Leak: Generic Password Rule
        String pass = "SuperSecretPassword123!"; 

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + ipAddress + "/db", user, pass);
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM users WHERE name = '" + user + "'";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}