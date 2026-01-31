@RestController
public class SecretController {
    // GitHub token should be provided via environment variable or secure vault
    private String githubToken = System.getenv("GITHUB_TOKEN");
}
