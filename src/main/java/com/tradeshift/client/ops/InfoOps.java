package com.tradeshift.client.ops;

import java.util.UUID;

import org.codehaus.jackson.JsonNode;

import com.tradeshift.client.TradeshiftRestClient;

/**
 * API Operations about public information, that can be retrieved without authenticating.
 */
public class InfoOps {
    public static InfoOps on (TradeshiftRestClient client) {
        return new InfoOps(client);
    }
    
    private final TradeshiftRestClient client;

    protected InfoOps(TradeshiftRestClient client) {
        this.client = client;
    }

    /**
     * Returns the "cluster ID" of the Tradeshift instance this REST client is talking to. The Cluster ID uniquely
     * identifies a Tradeshift environment, e.g. production, sandbox, or others.
     */
    public UUID getClusterId() {
        JsonNode status = client.getJson(
            client.resource()
                  .path("external/info/status"));
        return UUID.fromString(status.get("ClusterId").asText());
    }
}
