package com.tradeshift.client;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonNode;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

public abstract class RestClient {
    /**
     * Gets a jersey WebResource. The user can configure it for sub-paths, and then pass it to one of the other methods of this interface.
     * Do not call .get() etc. on the resource itself, as that will not insert the required headers.
     */
    public abstract WebResource resource();

    /**
     * Does a http GET on the given resource.
     */
    public abstract <T> T get (Class<T> type, Builder builder);
    
    /**
     * Does a http GET on the given resource.
     */
    public <T> T get(Class<T> type, WebResource resource) {
        return get(type, resource.getRequestBuilder());
    }
    
    /**
     * Does a http GET on the given resource, requesting the result as JSON.
     */
    public JsonNode getJson(WebResource resource) {
        return get(JsonNode.class, resource.accept(MediaType.APPLICATION_JSON_TYPE));
    }
    
    /**
     * Does a http POST on the given resource, with no body.
     */
    public abstract void post(WebResource.Builder resource);
    
    /**
     * Does a http POST on the given resource, with no body.
     */
    public void post(WebResource resource) {
        post(resource.getRequestBuilder());
    }
    
    /**
     * Does a http POST on the given resource, with the given request entity as body.
     */
    public abstract void post(WebResource.Builder resource, Object requestEntity);
    
    /**
     * Does a http POST on the given resource, with the given request entity as body.
     */
    public void post(WebResource resource, Object requestEntity) {
        post(resource.getRequestBuilder(), requestEntity);
    }
    
    /**
     * Does a http PUT on the given resource, with no body.
     */
    public abstract void put(WebResource.Builder resource);
    
    /**
     * Does a http PUT on the given resource, with no body.
     */
    public void put(WebResource resource) {
        put(resource.getRequestBuilder());
    }
    
    /**
     * Does a http PUT on the given resource, with the given requestEntity as body.
     */
    public abstract void put(WebResource.Builder resource, Object requestEntity);
    
    /**
     * Does a http PUT on the given resource, with the given requestEntity as body.
     */
    public void put(WebResource resource, Object requestEntity) {
        put(resource.getRequestBuilder(), requestEntity);
    }
    
    /**
     * Does a http DELETE on the given resource.
     */
    public abstract void delete(WebResource.Builder resource);
    
    /**
     * Does a http DELETE on the given resource.
     */
    public void delete(WebResource resource) {
        delete(resource.getRequestBuilder());
    }
    
}
