package hello;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecretController {
    // Hardcoded API Token
    // Gitleaks/Trufflehog should flag this:
    private String githubToken = "ghp_nS1yOEcfP5pvfqJml36mF7AkyHsEU0IU36mF";
}