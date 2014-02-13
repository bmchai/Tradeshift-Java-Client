package com.tradeshift.client.oauth1.credentials;

public class TokenAndSecret {
    private final String token;
    private final String secret;
    
    public TokenAndSecret(String token, String secret) {
        this.token = token;
        this.secret = secret;
    }
    
    public String getToken() {
        return token;
    }
    
    public String getSecret() {
        return secret;
    }
}
