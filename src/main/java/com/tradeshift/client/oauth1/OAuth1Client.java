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
    public <T> T get(Class<T> type, WebResource.Builder builder) {
        return client.get(type, addHeaders(builder));
    }
    
    @Override
    public void post(WebResource.Builder resource) {
        client.post(addHeaders(resource));
    }
    
    @Override
    public void put(WebResource.Builder resource) {
        client.put(addHeaders(resource));
    }
    
    @Override
    public void put(Builder resource, Object requestEntity) {
        client.put(addHeaders(resource), requestEntity);
    }
    
    @Override
    public void delete(Builder resource) {
        client.delete(addHeaders(resource));
    }
    
    @Override
    public void post(Builder resource, Object requestEntity) {
        client.post(addHeaders(resource), requestEntity);
    }

    protected OAuthSecrets getOAuthSecrets() {
        return new OAuthSecrets();
    }

    protected OAuthParameters getOAuthParameters() {
        return new OAuthParameters().signatureMethod("HMAC-SHA1").version();
    }

    protected WebResource.Builder addHeaders(WebResource.Builder builder) {
        return builder;
    }
}
