package com.tradeshift.client.jersey;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonNode;
import org.w3c.dom.Document;

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
    
    /**
     * Gets the given web resource as XML, by sending an Accept:text/xml header,
     * and unmarshaling the response as a W3C DOM Document.
     */
    public static Document getXml (WebResource resource) {
        return resource.accept(MediaType.TEXT_XML).get(Document.class);
    }
}
