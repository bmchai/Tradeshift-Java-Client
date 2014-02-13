package com.tradeshift.client.oauth1.credentials;

import java.util.UUID;

public class OAuth1NoCredentialStorage implements OAuth1CredentialStorage {

    @Override
    public TokenAndSecret get(UUID companyAccountId) {
        return null;
    }

    @Override
    public void put(UUID companyAccountId, TokenAndSecret credentials) {
    }

    @Override
    public void remove(UUID companyAccountId) {
    }
}
