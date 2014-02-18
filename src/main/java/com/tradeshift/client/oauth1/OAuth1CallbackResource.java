package com.tradeshift.client.oauth1;

import java.util.UUID;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.QueryParam;

import com.tradeshift.client.oauth1.credentials.OAuth1CredentialsManager;
import com.tradeshift.client.oauth1.credentials.TokenAndSecret;

/**
 * Base class that can be the callback URL for a consumer key on Tradeshift.
 * 
 * Assuming that you're using Spring and Jersey, and you have a {@link OAuth1ConsumerClient} in your
 * application context, an example class could look like:
 * 
 * <pre><code>
 *    @Component
 *    @Path("/rest/callbacks/active")
 *    public class MyClass extends OAuth1CallbackResource {
 *        @Autowired private OAuth1ConsumerClient client;
 *    
 *        @Override protected OAuth1CredentialsManager getCredentialsManager() {
 *            return client.getCredentialsManager();
 *        }
 *    }
 *  
 * </code></pre>
 */
public abstract class OAuth1CallbackResource {
    protected abstract OAuth1CredentialsManager getCredentialsManager();
    
    @POST
    public void activated(@QueryParam("companyAccountId") UUID companyAccountId, 
                             @QueryParam("oauth_token") String token, 
                             @QueryParam("oauth_token_secret") String tokenSecret) {
        getCredentialsManager().store(companyAccountId, new TokenAndSecret(token, tokenSecret));
    }
    
    @DELETE
    public void deactivated(@QueryParam("companyAccountId") UUID companyAccountId) {
        getCredentialsManager().remove (companyAccountId);
    }
}
