package com.tradeshift.client.oauth1.credentials;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tradeshift.client.exception.TradeshiftClientException;
import com.tradeshift.client.oauth1.AccountOps;
import com.tradeshift.client.oauth1.OAuth1Client;

public class OAuth1CredentialsManager {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    
    private final OAuth1Client tradeshiftAPI;
    private final OAuth1CredentialStorage storage;
    
    public OAuth1CredentialsManager(OAuth1Client tradeshiftAPI, OAuth1CredentialStorage storage) {
        this.storage = storage;
        this.tradeshiftAPI = tradeshiftAPI;
    }
    
    public void store (UUID companyAccountId, TokenAndSecret credentials) {
        log.info ("Received Oauth callback for {} with token {}", companyAccountId, credentials.getToken());
        storage.put(companyAccountId, credentials);
        synchronized(this) {
            notifyAll();
        }
    }
    
    /**
     * Retrieve the OAuth credentials for the given company account ID, either by getting them from storage,
     * or by requesting the proxy to call us back with the information.
     * @param timeout Time in ms to wait for our callback, before returning null
     * @throws TradeshiftClientException if no credentials are received within the configured timeout.
     */
    public TokenAndSecret retrieve (UUID companyAccountId, long timeout) {
        long start = System.currentTimeMillis();
        TokenAndSecret credentials = storage.get(companyAccountId);
        if (credentials != null) return credentials;
        do {
            log.debug ("Requesting resend of token for {}", companyAccountId);
            try {
                new AccountOps(tradeshiftAPI).resendOAuthToken(companyAccountId);
            } catch (RuntimeException x) {
                log.error ("Error requesting resend of OAuth token for {}", companyAccountId, x);
                throw new TradeshiftClientException("Cannot reach Tradeshift", x); // Don't retry when we can't reach Tradeshift. 
            }
            synchronized(this) {
                long timeleft = Math.max (1, System.currentTimeMillis() - start);
                try {
                    wait(timeleft);
                } catch (InterruptedException e) {
                    log.warn ("Interrupted while waiting on OAuth callback for company account {}", companyAccountId, e);
                    throw new TradeshiftClientException("Interrupted while waiting for callback", e);
                }
            }
            credentials = storage.get(companyAccountId);
            if (credentials != null) return credentials;
        } while (System.currentTimeMillis() < start + timeout);
        throw new TradeshiftClientException("Timed out waiting for credentials on company account " + companyAccountId + 
                " after " + timeout + "ms");
    }

    /**
     * Remove the given company account id from the storage.
     */
    public void remove(UUID companyAccountId) {
        storage.remove(companyAccountId);        
    }

}
