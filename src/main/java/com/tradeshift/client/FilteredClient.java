package com.tradeshift.client;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.ext.Providers;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.ClientFilter;
import com.sun.jersey.api.client.filter.LoggingFilter;

/**
 * Wraps a Jersey client with a set of filters to apply to each resource.
 * We use this method in order to be able to re-use the same client and connection pool,
 * while varying the applied filters based on context. 
 */
public class FilteredClient {
    private static final Logger log = LoggerFactory.getLogger(FilteredClient.class);
    
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
    
    public static FilteredClient create(String baseUrl) {
        return new FilteredClient(createClient(), baseUrl, new ArrayList<ClientFilter>());
    }
    
    private final String baseUrl;
    private final Client client;
    private final List<ClientFilter> filters;
    
    private FilteredClient(Client client, String baseUrl, List<ClientFilter> filters) {
        this.client = client;
        this.baseUrl = baseUrl;
        this.filters = filters;
    }
    
    /**
     * Returns a new FilteredClient that will apply the given filter at the end of the chain.
     */
    public FilteredClient filtered(ClientFilter filter) {
        ArrayList<ClientFilter> f = new ArrayList<>(filters);
        f.add(filter);
        return new FilteredClient(client, baseUrl, f);
    }
    
    /**
     * Gets a jersey WebResource, relative to the base URL of this client, to which all registered
     * filters are applied.
     */
    public WebResource resource() {
        WebResource resource = client.resource(baseUrl);

        if (log.isTraceEnabled()) {
            resource.addFilter(new LoggingFilter());
        }
        
        for (ClientFilter filter: filters) {
            resource.addFilter(filter);
        }
        
        return resource;
    }

    public Providers getJaxRsProviders() {
        return client.getProviders();
    }

    
}
