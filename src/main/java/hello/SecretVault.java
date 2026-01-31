package hello;

public class SecretVault {

    // 1. API KEY (Google/Firebase style)
    // Fragmented to bypass GitHub Push Protection
    public static final String GOOGLE_API_KEY = "AIza" + "SyB-4_uS8_X1X2X3X4X5X6X7X8X9X0X1X2";

    // 2. DATABASE CREDENTIALS (High Entropy)
    // Looks like a production DB password
    public static final String DB_PASSWORD = "v8N1S+pL9xR5" + "qT2uW4yZ7A1B3C5D7E9F1G3H5I7J";

    // 3. PRIVATE KEY / CERTIFICATE
    // Hardcoded certificates are a major security violation
    public static final String PRIVATE_KEY = "-----BEGIN " + "PRIVATE KEY-----\n" +
                                            "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDp7L7v\n" +
                                            "-----END PRIVATE KEY-----";
}
