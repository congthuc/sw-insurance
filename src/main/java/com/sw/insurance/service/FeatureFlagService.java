package com.sw.insurance.service;

import com.launchdarkly.sdk.LDContext;
import com.launchdarkly.sdk.LDUser;
import com.launchdarkly.sdk.server.LDClient;
import org.springframework.stereotype.Service;

@Service
public class FeatureFlagService {

    private final LDClient ldClient;

    private static final LDContext DEFAULT_CONTEXT = LDContext.builder("default-context")
            .name("Default User")
            .build();

    private static final LDUser DEFAULT_USER = new LDUser.Builder("default-user").build();

    public FeatureFlagService(LDClient ldClient) {
        this.ldClient = ldClient;
    }

    public boolean isFeatureEnabled(String featureKey) {
        return ldClient.boolVariation(featureKey, DEFAULT_USER, false);
    }

    public boolean isFeatureEnabled(String featureKey, boolean defaultValue) {
        return ldClient.boolVariation(featureKey, DEFAULT_CONTEXT, defaultValue);
    }

    public boolean isFeatureEnabled(String featureKey, String userId) {
        LDUser user = new LDUser.Builder(userId).build();
        return ldClient.boolVariation(featureKey, user, false);
    }

    public boolean isFeatureEnabled(String featureKey, String userId, boolean defaultValue) {
        LDUser user = new LDUser.Builder(userId).build();
        return ldClient.boolVariation(featureKey, user, defaultValue);
    }
}
