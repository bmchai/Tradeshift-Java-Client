package com.tradeshift.client.oauth1;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.oauth.client.OAuthClientFilter;
import com.sun.jersey.oauth.signature.OAuthParameters;
import com.sun.jersey.oauth.signature.OAuthSecrets;
import com.tradeshift.client.RestClient;
import com.tradeshift.client.TradeshiftRestClient;

/**
 * Base class for a RestClient that signs requests using OAuth 1
 */
public abstract class OAuth1Client extends RestClient {
    
    protected final TradeshiftRestClient client;
    
    protected OAuth1Client(TradeshiftRestClient client) {
        this.client = client;
    }
    
    @Override
    public WebResource resource() {
        OAuthParameters params = getOAuthParameters();
        OAuthSecrets secrets = getOAuthSecrets();
        OAuthClientFilter filter = new OAuthClientFilter(TradeshiftRestClient.Internal.getJaxRsProviders(client), params, secrets);

        WebResource resource = client.resource();
        resource.addFilter(filter);
        return resource;
    }

    @Override
    public <T> T get(Class<T> type, Builder builder) {
        return client.get(type, addHeaders(builder));
    }
    
    @Override
    public void post(WebResource.Builder resource) {
        client.post(addHeaders(resource));
    }

    protected OAuthSecrets getOAuthSecrets() {
        return new OAuthSecrets();
    }

    protected OAuthParameters getOAuthParameters() {
        return new OAuthParameters().signatureMethod("HMAC-SHA1").version();
    }

    protected Builder addHeaders(Builder builder) {
        return builder;
    }
}
