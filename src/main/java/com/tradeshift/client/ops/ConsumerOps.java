package com.tradeshift.client.ops;

import java.util.UUID;

import com.tradeshift.client.oauth1.OAuth1ConsumerClient;

/**
 * API Operations that are about OAuth1 consumers (under external/consumer), which are to be called
 * only as a consumer + key, without a token.
 */
public class ConsumerOps {
    public static ConsumerOps on (OAuth1ConsumerClient client) {
        return new ConsumerOps(client);
    }
    
    private final OAuth1ConsumerClient client;

    protected ConsumerOps(OAuth1ConsumerClient client) {
        this.client = client;
    }
    
    /**
     * Requests a re-send of the OAuth token to access the given company account ID. The token will
     * be sent to the URL configured in the developer interface for the current consumer key.
     */
    public void resendOAuthToken(UUID companyAccountId) {
        client.resource()
           .path("external/consumer/accounts")
           .path(companyAccountId.toString())
           .path("resendtoken")
           .post();
    }

}
