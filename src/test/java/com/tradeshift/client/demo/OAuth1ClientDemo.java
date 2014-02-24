package com.tradeshift.client.demo;

import com.tradeshift.client.TradeshiftRestClient;
import com.tradeshift.client.oauth1.OAuth1TokenClient;
import com.tradeshift.client.ops.AccountOps;
import com.tradeshift.client.ops.InfoOps;

public class OAuth1ClientDemo {

    public static void main(String[] args) {
        TradeshiftRestClient client = TradeshiftRestClient.production("/// Put-Your-User-Agent-Here ///");
        System.out.println("Tradeshift production cluster ID = " + InfoOps.on(client).getClusterId());
        
        // Find the values to put here by installing the TS OwnAccount app
        // https://go.tradeshift.com/apps/view/Tradeshift.APIAccessToOwnAccount
        OAuth1TokenClient account = client.forOwnAccount("V36skaS2-nGfQYj+9qRFmb@@5K26R2", "F3-ZCmS3rKCp-rBdAFQm2zH6uQRZHBEZnrXC+G6a");
        System.out.println("My account info: " + AccountOps.on(account).getInfo());
    }
}
