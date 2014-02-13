package com.tradeshift.client.oauth1;

import java.util.UUID;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.QueryParam;

import com.tradeshift.client.oauth1.credentials.OAuth1CredentialsManager;
import com.tradeshift.client.oauth1.credentials.TokenAndSecret;

public abstract class OAuth1CallbackResource {
    protected abstract OAuth1CredentialsManager getCredentialsManager();
    
    @POST
    public void activatedSsh(@QueryParam("companyAccountId") UUID companyAccountId, 
                             @QueryParam("oauth_token") String token, 
                             @QueryParam("oauth_token_secret") String tokenSecret) {
        getCredentialsManager().store(companyAccountId, new TokenAndSecret(token, tokenSecret));
    }
    
    @DELETE
    public void deactivatedSsh(@QueryParam("companyAccountId") UUID companyAccountId) {
        getCredentialsManager().remove (companyAccountId);
    }
}
