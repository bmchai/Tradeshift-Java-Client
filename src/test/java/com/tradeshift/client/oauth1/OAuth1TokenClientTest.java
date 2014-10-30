package com.tradeshift.client.oauth1;

import com.sun.jersey.core.util.StringKeyIgnoreCaseMultivaluedMap;
import com.tradeshift.client.JerseyClient;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.ws.rs.core.MultivaluedMap;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class OAuth1TokenClientTest {

    @Test
    public void client_should_add_user_id_in_header_when_client_is_created_with_user_id() {
        JerseyClient client = mock(JerseyClient.class);
        when(client.filtered(any(JerseyClient.HeaderProcessor.class))).thenReturn(client);

        UUID companyId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        OAuth1TokenClient oAuth1TokenClient = new OAuth1TokenClient(client, "consumerKey", "consumerSecret", "token", "tokenSecret", companyId);
        oAuth1TokenClient.withUserId(userId);

        ArgumentCaptor<JerseyClient.HeaderProcessor> processorArg = ArgumentCaptor.forClass(JerseyClient.HeaderProcessor.class);
        // filtered gets called twice, while building oauth1 client.
        verify(client, times(2)).filtered(processorArg.capture());
        JerseyClient.HeaderProcessor processor = processorArg.getValue();
        MultivaluedMap<String, Object> headers = new StringKeyIgnoreCaseMultivaluedMap<>();
        processor.processHeaders(headers);

        assertEquals(userId.toString(), headers.getFirst("X-Tradeshift-ActorId"));
        assertEquals(companyId.toString(), headers.getFirst("X-Tradeshift-TenantId"));
    }
}
