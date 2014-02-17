package com.tradeshift.client.oauth1;

import com.tradeshift.client.TradeshiftRestClient;
import com.tradeshift.client.ops.AccountOps;
import com.tradeshift.client.ops.InfoOps;

public class OAuth1ClientTest {

    public static void main(String[] args) {
        TradeshiftRestClient client = TradeshiftRestClient.production("/// Put-Your-User-Agent-Here ///");
        System.out.println("Tradeshift production cluster ID = " + InfoOps.on(client).getClusterId());
        
        // Find the values to put here by installing the TS OwnAccount app
        // https://go.tradeshift.com/apps/view/Tradeshift.APIAccessToOwnAccount
        OAuth1TokenClient account = client.forOwnAccount("put-your-token-here", "put-your-secret-here");
        System.out.println("My account info: " + AccountOps.on(account).getInfo());
    }
}
