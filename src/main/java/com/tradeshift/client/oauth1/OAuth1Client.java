package com.tradeshift.client.oauth1;

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.oauth.client.OAuthClientFilter;
import com.sun.jersey.oauth.signature.OAuthParameters;
import com.sun.jersey.oauth.signature.OAuthSecrets;
import com.tradeshift.client.JerseyClient;
import com.tradeshift.client.JerseyClient.HeaderProcessor;

/**
 * Base class for a RestClient that signs requests using OAuth 1
 */
public abstract class OAuth1Client {
    
    protected final JerseyClient client;
    
    protected OAuth1Client(JerseyClient client) {
        this.client = client.filtered(new HeaderProcessor() {
            @Override
            public void processHeaders(MultivaluedMap<String, Object> headers) {
                applyHeaders(headers);
            }
        });
    }
    
    /**
     * Gets a jersey WebResource, relative to the base URL of this client, on which the OAuth authentication will be applied.
     */
    public WebResource resource() {
        OAuthParameters params = getOAuthParameters();
        OAuthSecrets secrets = getOAuthSecrets();
        OAuthClientFilter filter = new OAuthClientFilter(client.getJaxRsProviders(), params, secrets);

        WebResource resource = client.resource();
        resource.addFilter(filter);
        return resource;
    }
    
    protected OAuthSecrets getOAuthSecrets() {
        return new OAuthSecrets();
    }

    protected OAuthParameters getOAuthParameters() {
        return new OAuthParameters().signatureMethod("HMAC-SHA1").version();
    }

    protected void applyHeaders(MultivaluedMap<String, Object> headers) {
        
    }
}
