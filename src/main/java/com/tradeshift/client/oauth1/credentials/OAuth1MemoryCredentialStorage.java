package com.tradeshift.client.oauth1.credentials;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class OAuth1MemoryCredentialStorage implements OAuth1CredentialStorage {
    private static final Logger log = LoggerFactory.getLogger(OAuth1MemoryCredentialStorage.class); 
    
    private final Cache<UUID, TokenAndSecret> cache;
    private final OAuth1CredentialStorage secondLevel;

    public OAuth1MemoryCredentialStorage (int maxCacheSize, OAuth1CredentialStorage secondLevel) {
        this.cache = CacheBuilder.newBuilder().maximumSize(maxCacheSize).build();
        this.secondLevel = secondLevel;
    }
    
    @Override
    public TokenAndSecret get(UUID companyAccountId) {
        TokenAndSecret ours = cache.getIfPresent(companyAccountId);
        if (ours != null) {
            return ours;
        } else {
            return secondLevel.get(companyAccountId);
        }
    }

    @Override
    public void put(UUID companyAccountId, TokenAndSecret credentials) {
        log.info ("Received Oauth credentials for {} with token {}", companyAccountId, credentials.getToken());
        cache.put(companyAccountId, credentials);
        secondLevel.put(companyAccountId, credentials);
    }

    @Override
    public void remove(UUID companyAccountId) {
        cache.invalidate(companyAccountId);
        secondLevel.remove(companyAccountId);
    }
}
