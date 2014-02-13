package com.tradeshift.client.oauth1.credentials;

import java.util.UUID;

public interface OAuth1CredentialStorage {
    public TokenAndSecret get (UUID companyAccountId);
    public void put (UUID companyAccountId, TokenAndSecret credentials);
    public void remove (UUID companyAccountId);
}
