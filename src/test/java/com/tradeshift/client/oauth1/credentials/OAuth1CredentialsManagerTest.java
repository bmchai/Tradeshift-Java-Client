package com.tradeshift.client.oauth1.credentials;

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.api.client.WebResource;
import com.tradeshift.client.exception.TradeshiftClientException;
import com.tradeshift.client.oauth1.OAuth1ConsumerClient;

public class OAuth1CredentialsManagerTest {
    private ExecutorService background;
    private UUID companyAccountId;
    private OAuth1CredentialsManager m;

    @Before
    public void setup() {
        final ConcurrentHashMap<UUID, TokenAndSecret> credentials = new ConcurrentHashMap<UUID, TokenAndSecret>();
        OAuth1CredentialStorage storage = new OAuth1CredentialStorage() {
            @Override
            public void remove(UUID companyAccountId) {
                credentials.remove(companyAccountId);
            }
            
            @Override
            public void put(UUID companyAccountId, TokenAndSecret tokenAndSecret) {
                credentials.put(companyAccountId, tokenAndSecret);
            }
            
            @Override
            public TokenAndSecret get(UUID companyAccountId) {
                return credentials.get(companyAccountId);
            }
        };
        background = Executors.newFixedThreadPool(1);
        OAuth1ConsumerClient tradeshiftAPI = mock(OAuth1ConsumerClient.class);
        WebResource resource = mock(WebResource.class);
        when(resource.path(any(String.class))).thenReturn(resource);
        when(tradeshiftAPI.resource()).thenReturn(resource);
        m = new OAuth1CredentialsManager(tradeshiftAPI, storage);
        companyAccountId = UUID.randomUUID();        
    }
    
    @Test
    public void store_credential_should_unblock_a_waiting_retrieve_call() throws InterruptedException {
        final TokenAndSecret[] retrieved = new TokenAndSecret[1];
        background.execute(new Runnable() { public void run() {
            retrieved[0] = m.retrieve(companyAccountId, 1000);
        }});
        Thread.sleep(100); // wait for retrieve() to hit the wait block
        
        TokenAndSecret tokenAndSecret = new TokenAndSecret("token", "secret");
        m.store(companyAccountId, tokenAndSecret);
        background.shutdown();
        background.awaitTermination(2, TimeUnit.SECONDS);
        
        assertSame(tokenAndSecret, retrieved[0]);
    }
    
    @Test(expected = TradeshiftClientException.class)
    public void retrieve_should_fail_if_no_store_is_invoked_within_timeout() {
        m.retrieve(companyAccountId, 100);
    }
}
