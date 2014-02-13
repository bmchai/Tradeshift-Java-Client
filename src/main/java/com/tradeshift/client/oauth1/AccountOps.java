package com.tradeshift.client.oauth1;

import java.util.UUID;

import com.tradeshift.client.RestClient;

public class AccountOps {
    private final RestClient api;

    public AccountOps(RestClient api) {
        this.api = api;
    }
    
    public void resendOAuthToken(UUID companyAccountId) {
        api.post (
            api.resource()
               .path("external/consumer/accounts")
               .path(companyAccountId.toString())
               .path("resendtoken")
        );
    }

}
