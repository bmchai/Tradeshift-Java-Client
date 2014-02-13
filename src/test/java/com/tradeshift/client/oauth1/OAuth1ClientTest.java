package com.tradeshift.client.oauth1;

import com.tradeshift.client.TradeshiftRestClient;

public class OAuth1ClientTest {

    public void test() {
        TradeshiftRestClient.production("Tradeshift-Java-Client-1.0").forOAuth1("sshunittestkey", "secret").withToken("toek", "sec");
    }
}
