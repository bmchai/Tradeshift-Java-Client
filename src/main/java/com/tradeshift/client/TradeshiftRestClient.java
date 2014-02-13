package com.tradeshift.client;

import java.util.UUID;

import javax.ws.rs.ext.Providers;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.tradeshift.client.oauth1.OAuth1ConsumerClient;
import com.tradeshift.client.oauth1.credentials.OAuth1CredentialStorage;
import com.tradeshift.client.oauth1.credentials.OAuth1MemoryCredentialStorage;
import com.tradeshift.client.oauth1.credentials.OAuth1NoCredentialStorage;

public class TradeshiftRestClient {
    private final Logger log = LoggerFactory.getLogger(getClass());
    
    public static TradeshiftRestClient production(String userAgent) {
        return new TradeshiftRestClient(userAgent, createClient(), "https://api.tradeshift.com/tradeshift/rest");
    }
    
    private static Client createClient() {
        ClientConfig cc = new DefaultClientConfig();
        cc.getClasses().add(JacksonJsonProvider.class);
        Client client = Client.create(cc);
        client.setFollowRedirects(false);
        client.setChunkedEncodingSize(64 * 1024);
        client.setConnectTimeout(10000);
        client.setReadTimeout(60000);
        return client;
    }
    
    private final String userAgent;
    private final Client client;
    private final String baseURL;
    
    private TradeshiftRestClient(String userAgent, Client client, String baseURL) {
        this.userAgent = userAgent;
        this.client = client;
        this.baseURL = baseURL;
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
        return OAuth1ConsumerClient.of(this, consumerKey, consumerSecret, 10000, storage);
    }
    
    /**
     * Returns the X-Tradeshift-RequestId to send to Tradeshift, or null to not send a request ID. The request ID
     * is used by Tradeshift to better implement idempotency for certain requests, in case of re-tries.
     */
    protected UUID getRequestId() {
        return null;
    }
    
    /**
     * Gets a jersey WebResource, relative to the base URL of this client.
     */
    public WebResource resource() {
        WebResource resource = client.resource(baseURL);

        if (log.isTraceEnabled()) {
            resource.addFilter(new LoggingFilter());
        }

        return resource;
    }

    /**
     * Makes a request for the given request builder, using POST.
     */
    public void post(Builder builder) {
        addHeaders(builder).post();
    }
    
    /**
     * Adds Tradeshift headers to the given request.
     */
    protected Builder addHeaders(Builder b) {
        b = b.header("User-Agent", userAgent);
        UUID requestId = getRequestId();
        if (requestId != null) {
            b = b.header("X-Tradeshift-RequestId", requestId.toString());
        }
        return b;
    }
    
    /** 
     * Returns the Providers used by the wrapped Jersey client.
     */
    public Providers getProviders() {
        return client.getProviders();
    }

}
