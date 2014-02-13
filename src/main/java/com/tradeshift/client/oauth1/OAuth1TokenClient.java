package com.tradeshift.client.oauth1;

import java.util.UUID;

import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.oauth.signature.OAuthParameters;
import com.sun.jersey.oauth.signature.OAuthSecrets;
import com.tradeshift.client.TradeshiftRestClient;

/**
 * OAuth 1 Rest client that signs requests using consumer key, consumer secret, token, and token secret.
 */
public class OAuth1TokenClient extends OAuth1Client {

    protected final String consumerKey;
    protected final String consumerSecret;
    private final String token;
    private final String tokenSecret;
    private final UUID tenantId;
    
    OAuth1TokenClient(TradeshiftRestClient client, String consumerKey, String consumerSecret, String token, String tokenSecret, UUID tenantId) {
        super(client);
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.token = token;
        this.tokenSecret = tokenSecret;
        this.tenantId = tenantId; 
    }
    
    public OAuth1TokenClient withTenantId(UUID tenantId) {
        return new OAuth1TokenClient(client, consumerKey, consumerSecret, token, tokenSecret, tenantId);
    }
    
    @Override
    protected OAuthSecrets getOAuthSecrets() {
        return super.getOAuthSecrets().consumerSecret(consumerSecret).tokenSecret(tokenSecret);
    }

    @Override
    protected OAuthParameters getOAuthParameters() {
        return super.getOAuthParameters().consumerKey(consumerKey).token(token);
    }
    
    @Override
    protected Builder addHeaders(Builder builder) {
        if (tenantId == null) {
            return builder;
        } else {
            return builder.header("X-Tradeshift-TenantId", tenantId.toString());            
        }
    }

}
