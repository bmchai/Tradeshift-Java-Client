package com.tradeshift.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

import java.util.UUID;

import javax.ws.rs.core.MultivaluedMap;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.google.common.base.Supplier;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.StringKeyIgnoreCaseMultivaluedMap;
import com.tradeshift.client.JerseyClient.HeaderProcessor;

public class TradeshiftRestClientTest {
    
    @Test
    public void client_adds_user_agent_and_request_id_headers_to_resource() {
        JerseyClient client = mock(JerseyClient.class);
        JerseyClient filteredClient = mock(JerseyClient.class);
        when(client.filtered(any(HeaderProcessor.class))).thenReturn(filteredClient);
        
        WebResource resource = mock(WebResource.class);
        when(filteredClient.resource()).thenReturn(resource);
        
        final UUID requestId = UUID.randomUUID();
        TradeshiftRestClient c = TradeshiftRestClient
            .of(client, "userAgent")
            .withRequestId(new Supplier<UUID>() {
                public UUID get() {
                    return requestId;
                }});
        WebResource r = c.resource();
        assertSame(resource, r);
        
        ArgumentCaptor<HeaderProcessor> processorArg = ArgumentCaptor.forClass(HeaderProcessor.class);
        // filtered gets called twice, while building TradeshiftRestClient. The first filter gets replaced.
        verify(client, times(2)).filtered(processorArg.capture());
        HeaderProcessor processor = processorArg.getValue();
        MultivaluedMap<String, Object> headers = new StringKeyIgnoreCaseMultivaluedMap<>();
        processor.processHeaders(headers);
        
        assertEquals("userAgent", headers.getFirst("User-Agent"));
        assertEquals(requestId.toString(), headers.getFirst("X-Tradeshift-RequestId"));
    }
}
