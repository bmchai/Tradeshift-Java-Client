package com.tradeshift.client;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.ClientFilter;
import com.tradeshift.client.JerseyClient.HeaderProcessor;
import com.tradeshift.client.JerseyClient.HeaderProcessorClientFilter;

public class JerseyClientTest {
    @Test
    public void client_applies_filter_and_base_url_to_resource() {
        Client client = mock(Client.class);
        WebResource resource = mock(WebResource.class);
        when(client.resource("base")).thenReturn(resource);
        HeaderProcessor processor = mock(HeaderProcessor.class);
        JerseyClient c = new JerseyClient(client, "base", new ArrayList<ClientFilter>(), 10, 10).filtered(processor);
        
        WebResource r = c.resource();
        assertSame(resource, r);
        ArgumentCaptor<HeaderProcessorClientFilter> filterArg = ArgumentCaptor.forClass(HeaderProcessorClientFilter.class);
        verify(resource).addFilter(filterArg.capture());
        assertSame(processor, filterArg.getValue().processor);
    }

    @Test
    public void creating_client_applies_connection_and_read_timeout() {
        final Client client = mock(Client.class);
        new JerseyClient(null, "base", new ArrayList<ClientFilter>(), 1, 2) {
            protected Client createClient() {
                return client;
            };
        };
        verify(client).setConnectTimeout(1);
        verify(client).setReadTimeout(2);
    }
}
