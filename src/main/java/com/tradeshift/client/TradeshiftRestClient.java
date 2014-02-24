package com.tradeshift.client;

import java.util.UUID;

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.api.client.WebResource;
import com.tradeshift.client.JerseyClient.HeaderProcessor;
import com.tradeshift.client.oauth1.OAuth1ConsumerClient;
import com.tradeshift.client.oauth1.OAuth1TokenClient;
import com.tradeshift.client.oauth1.credentials.OAuth1CredentialStorage;
import com.tradeshift.client.oauth1.credentials.OAuth1MemoryCredentialStorage;
import com.tradeshift.client.oauth1.credentials.OAuth1NoCredentialStorage;

/**
 * Entry point for creating Tradeshift REST client instances.
 */
public class TradeshiftRestClient {
    
    /**
     * Creates a new TradeshiftRestClient for accessing the Tradeshift production server.
     * @param userAgent The HTTP User-Agent to use. Must be uniquely identifying you as an API user.
     */
    public static TradeshiftRestClient production(String userAgent) {
        return of(JerseyClient.production(), userAgent);
    }
    
    /**
     * Creates a new TradeshiftRestClient for accessing the Tradeshift sandbox server.
     * @param userAgent The HTTP User-Agent to use. Must be uniquely identifying you as an API user.
     */
    public static TradeshiftRestClient sandbox(String userAgent) {
        return of(JerseyClient.production(), userAgent);
    }
    
    /**
     * Creates a new TradeshiftRestClient for accessing the Tradeshift API.
     * @param client The JerseyClient instance used to make requests
     * @param userAgent The HTTP User-Agent to use. Must be uniquely identifying you as an API user.
     */
    public static TradeshiftRestClient of(JerseyClient client, String userAgent) {
        return new TradeshiftRestClient(client, userAgent);
    }
    
    protected final JerseyClient client;
    
    protected TradeshiftRestClient(JerseyClient client, final String userAgent) {
        this.client = client.filtered(new HeaderProcessor() {
            @Override
            public void processHeaders(MultivaluedMap<String, Object> headers) {
                headers.putSingle("User-Agent", userAgent);
                UUID requestId = getRequestId();
                if (requestId != null) {
                    headers.putSingle("X-Tradeshift-RequestId", requestId.toString());
                }                
            }
        });
    }
    
    /**
     * Creates a new OAuth1 client, with an in-memory credential storage for 1000 users, and an
     * account retrieval timeout of 10 seconds.
     */
    public OAuth1ConsumerClient forOAuth1(String consumerKey, String consumerSecret) {
        OAuth1CredentialStorage storage = new OAuth1MemoryCredentialStorage(1000, new OAuth1NoCredentialStorage());
        return forOAuth1(consumerKey, consumerSecret, storage);
    }
    
    /**
     * Creates a new OAuth1 client, with the given credential storage, and an
     * account retrieval timeout of 10 seconds.
     */
    public OAuth1ConsumerClient forOAuth1(String consumerKey, String consumerSecret, OAuth1CredentialStorage storage) {
        return OAuth1ConsumerClient.of(client, consumerKey, consumerSecret, 10000, storage);
    }
    
    /**
     * Creates a new OAuth1 client for accessing the "OwnAccount" app, by using
     * both consumer key and consumer secret "OwnAccount".
     * 
     * In order to use this call, install the "OwnAccount" app on tradeshift, and
     * copy the token and token secret values from there.
     * @param token The OAuth token 
     */
    public OAuth1TokenClient forOwnAccount(String token, String tokenSecret) {
        final String ownAccountApp_consumerKey = "OwnAccount";
        final String ownAccountApp_consumerSecret = "OwnAccount";
        return forOAuth1(ownAccountApp_consumerKey, ownAccountApp_consumerSecret).withToken(token, tokenSecret);
    }
    
    /**
     * Returns the X-Tradeshift-RequestId to send to Tradeshift, or null to not send a request ID. The request ID
     * is used by Tradeshift to better implement idempotency for certain requests, in case of re-tries.
     * 
     * You can implement this in sub-classes to return e.g. a thread-local value that covers your request context.
     */
    protected UUID getRequestId() {
        return null;
    }
    
    /**
     * Gets a jersey WebResource, relative to the base URL of this client.
     */
    public WebResource resource() {
        return client.resource();
    }
}
