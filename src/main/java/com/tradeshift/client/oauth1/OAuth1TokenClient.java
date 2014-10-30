package com.tradeshift.client.oauth1;

import java.util.UUID;

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.oauth.signature.OAuthParameters;
import com.sun.jersey.oauth.signature.OAuthSecrets;
import com.tradeshift.client.JerseyClient;

/**
 * OAuth 1 Rest client that signs requests using consumer key, consumer secret, token, and token secret.
 */
public class OAuth1TokenClient extends OAuth1Client {

    protected final String consumerKey;
    protected final String consumerSecret;
    private final String token;
    private final String tokenSecret;
    private final UUID tenantId;
    private UUID userId;
    
    OAuth1TokenClient(JerseyClient client, String consumerKey, String consumerSecret, String token, String tokenSecret, UUID tenantId) {
        super(client);
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.token = token;
        this.tokenSecret = tokenSecret;
        this.tenantId = tenantId;
    }

    OAuth1TokenClient(JerseyClient client, String consumerKey, String consumerSecret, String token, String tokenSecret, UUID tenantId, UUID userId) {
        this(client, consumerKey, consumerSecret, token, tokenSecret, tenantId);
        this.userId = userId;
    }
    
    public OAuth1TokenClient withTenantId(UUID tenantId) {
        return new OAuth1TokenClient(client, consumerKey, consumerSecret, token, tokenSecret, tenantId);
    }

    public OAuth1TokenClient withUserId(UUID userId) {
        return new OAuth1TokenClient(client, consumerKey, consumerSecret, token, tokenSecret, tenantId, userId);
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
    protected void applyHeaders(MultivaluedMap<String, Object> headers) {
        if (tenantId != null) {
            headers.putSingle("X-Tradeshift-TenantId", tenantId.toString());
        }
        if (userId != null) {
            headers.putSingle("X-Tradeshift-ActorId", userId.toString());
        }
    }
}
