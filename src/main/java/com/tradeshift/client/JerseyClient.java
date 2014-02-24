package com.tradeshift.client;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Providers;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
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
public class JerseyClient {
    private static final Logger log = LoggerFactory.getLogger(JerseyClient.class);
    
    /** Creates a new JerseyClient to the given base URL */
    public static JerseyClient to(String baseUrl) {
        return new JerseyClient(null, baseUrl, new ArrayList<ClientFilter>(), 10000, 60000);
    }
    
    /** Creates a new JerseyClient to the Tradeshift production URL */
    public static JerseyClient production() {
        return to("https://api.tradeshift.com/tradeshift/rest");
    }
    
    /** Creates a new JerseyClient to the Tradeshift sandbox URL */
    public static JerseyClient sandbox() {
        return to("https://api-sandbox.tradeshift.com/tradeshift/rest");
    }
    
    private final Client client;
    private final String baseUrl;
    private final List<ClientFilter> filters;
    private final int connectTimeout;
    private final int readTimeout;
    
    protected JerseyClient(Client client, String baseUrl, List<ClientFilter> filters, int connectTimeout, int readTimeout) {
        this.baseUrl = baseUrl;
        this.filters = filters;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
        if (client != null) {
            this.client = client;
        } else {
            this.client = createClient();
            initializeClient();
        }
    }
    
    protected void initializeClient() {
        client.setFollowRedirects(false);
        client.setChunkedEncodingSize(64 * 1024);
        client.setConnectTimeout(connectTimeout);
        client.setReadTimeout(readTimeout);        
    }
    
    protected Client createClient() {
        ClientConfig cc = new DefaultClientConfig();
        cc.getClasses().add(JacksonJsonProvider.class);
        return Client.create(cc);
    }

    /**
     * Returns a new JerseyClient that will apply the given header processor at the end of the chain.
     */
    public JerseyClient filtered(final HeaderProcessor headerProcessor) {
        ArrayList<ClientFilter> f = new ArrayList<>(filters);
        f.add(new HeaderProcessorClientFilter(headerProcessor));
        return new JerseyClient(client, baseUrl, f, connectTimeout, readTimeout);
    }
    
    /**
     * Returns a new JerseyClient that uses the given connect timeout, in milliseconds.
     */
    public JerseyClient withConnectTimeout(int connectTimeout) {
        return new JerseyClient(null, baseUrl, filters, connectTimeout, readTimeout);
    }
    
    /**
     * Returns a new JerseyClient that uses the given read timeout, in milliseconds.
     */
    public JerseyClient withReadTimeout(int readTimeout) {
        return new JerseyClient(null, baseUrl, filters, connectTimeout, readTimeout);
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

    public interface HeaderProcessor {
        public void processHeaders(MultivaluedMap<String, Object> headers);
    }
    
    class HeaderProcessorClientFilter extends ClientFilter {
        public final HeaderProcessor processor;

        public HeaderProcessorClientFilter(HeaderProcessor processor) {
            this.processor = processor;
        }

        @Override
        public ClientResponse handle(ClientRequest cr) {
            processor.processHeaders(cr.getHeaders());
            return getNext().handle(cr);
        }
    }
}
