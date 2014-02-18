package com.tradeshift.client.oauth1;

import java.util.UUID;

import com.sun.jersey.oauth.signature.OAuthParameters;
import com.sun.jersey.oauth.signature.OAuthSecrets;
import com.tradeshift.client.FilteredClient;
import com.tradeshift.client.exception.TradeshiftClientException;
import com.tradeshift.client.oauth1.credentials.OAuth1CredentialStorage;
import com.tradeshift.client.oauth1.credentials.OAuth1CredentialsManager;
import com.tradeshift.client.oauth1.credentials.TokenAndSecret;

/**
 * OAuth 1 Rest client that signs requests using only consumer key and secret.
 */
public class OAuth1ConsumerClient extends OAuth1Client {
    
    public static OAuth1ConsumerClient of(FilteredClient client, String consumerKey, String consumerSecret, 
                                          long accountRetrievalTimeout, OAuth1CredentialStorage storage) {
        return new OAuth1ConsumerClient(client, consumerKey, consumerSecret, null, storage, accountRetrievalTimeout);
    }
    
    protected final String consumerKey;
    protected final String consumerSecret;
    protected final OAuth1CredentialsManager credentialsManager;
    private final long accountRetrievalTimeout;
    
    protected OAuth1ConsumerClient(FilteredClient client, String consumerKey, String consumerSecret, 
                                   OAuth1CredentialsManager credentialsManager, OAuth1CredentialStorage storage, long accountRetrievalTimeout) {
        super(client);
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        if (credentialsManager == null) {
            this.credentialsManager = new OAuth1CredentialsManager(this, storage);
        } else {            
            this.credentialsManager = credentialsManager;
        }
        this.accountRetrievalTimeout = accountRetrievalTimeout;
    }
    
    /**
     * Returns a new OAuth1ConsumerClient which waits the given amount of milliseconds when requesting
     * account access tokens in its toCompanyAccount() method.
     */
    public OAuth1ConsumerClient withAccountRetrievalTimeout(long timeout) {
        return new OAuth1ConsumerClient(client, consumerKey, consumerSecret, credentialsManager, null, timeout);        
    }

    /**
     * Returns a client that accesses a specific company account using its token and token secret
     */
    public OAuth1TokenClient withToken(String token, String tokenSecret) {
        return new OAuth1TokenClient(client, consumerKey, consumerSecret, token, tokenSecret, null);
    }
    
    /**
     * Returns a client that can access the given company account
     * @throws TradeshiftClientException if no credentials are received within the configured timeout.
     */
    public OAuth1TokenClient toCompanyAccount(UUID companyAccountId) {
        TokenAndSecret credentials = credentialsManager.retrieve(companyAccountId, accountRetrievalTimeout);
        return new OAuth1TokenClient(client, consumerKey, consumerSecret, credentials.getToken(), credentials.getSecret(), null);
    }
    
    /**
     * Returns the credentials manager that keeps OAuth1 tokens and token secrets.
     */
    public OAuth1CredentialsManager getCredentialsManager() {
        return credentialsManager;
    }

    @Override
    protected OAuthSecrets getOAuthSecrets() {
        return super.getOAuthSecrets().consumerSecret(consumerSecret);
    }

    @Override
    protected OAuthParameters getOAuthParameters() {
        return super.getOAuthParameters().consumerKey(consumerKey);
    }
}
