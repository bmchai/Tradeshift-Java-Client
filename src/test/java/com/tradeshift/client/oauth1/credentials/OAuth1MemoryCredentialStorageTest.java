package com.tradeshift.client.oauth1.credentials;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OAuth1MemoryCredentialStorageTest {
    private OAuth1MemoryCredentialStorage c;
    private OAuth1CredentialStorage secondLevel;
    private UUID companyAccountId;

    @Before
    public void setup() {
        secondLevel = mock(OAuth1CredentialStorage.class);
        c = new OAuth1MemoryCredentialStorage(3, secondLevel);
        companyAccountId = UUID.randomUUID();
    }
    
    @Test
    public void get_should_invoke_2nd_level_if_absent() {
        c.get(companyAccountId);
        verify(secondLevel).get(companyAccountId);
    }
    
    @Test
    public void get_should_return_stored_value() {
        TokenAndSecret credentials = new TokenAndSecret("token", "secret");
        c.put(companyAccountId, credentials);
        assertEquals(credentials, c.get(companyAccountId));
    }
    
    @Test
    public void put_should_invoke_2nd_level() {
        TokenAndSecret credentials = new TokenAndSecret("token", "secret");
        c.put(companyAccountId, credentials);
        verify(secondLevel).put(companyAccountId, credentials);
    }
    
    @Test
    public void remove_should_invoke_2nd_level() {
        c.remove(companyAccountId);
        verify(secondLevel).remove(companyAccountId);
    }
}
