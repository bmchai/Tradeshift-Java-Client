package com.tradeshift.client.jersey;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonNode;

import com.sun.jersey.api.client.WebResource;

/**
 * Various utility methods for sending REST requests with Jersey
 */
public class JerseyUtil {

    /**
     * Gets the given web resource as JSON, by sending an Accept:application/json header,
     * and unmarshaling the response using Jackson.
     */
    public static JsonNode getJson (WebResource resource) {
        return resource.accept(MediaType.APPLICATION_JSON).get(JsonNode.class);
    }
}
