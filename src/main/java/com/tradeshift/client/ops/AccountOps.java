package com.tradeshift.client.ops;

import org.codehaus.jackson.JsonNode;

import com.tradeshift.client.oauth1.OAuth1TokenClient;

/**
 * API operations that are about the current account (user external/account) 
 */
public class AccountOps {
    public static AccountOps on (OAuth1TokenClient client) {
        return new AccountOps(client);
    }
    
    private final OAuth1TokenClient client;

    protected AccountOps(OAuth1TokenClient client) {
        this.client = client;
    }
    
    /** 
     * Gets information about the currently accessed account (external/account/info) 
     */
    public JsonNode getInfo() {
        return client.getJson(client.resource().path("external/account/info"));
    }
}
