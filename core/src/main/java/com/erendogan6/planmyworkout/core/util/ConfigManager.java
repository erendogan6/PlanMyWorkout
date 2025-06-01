package com.erendogan6.planmyworkout.core.util;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ConfigManager {

    private final FirebaseRemoteConfig remoteConfig;
    private String cachedPexelsApiKey;

    @Inject
    public ConfigManager(FirebaseRemoteConfig remoteConfig) {
        this.remoteConfig = remoteConfig;
    }

    /**
     * Get Pexels API key with caching
     */
    public String getPexelsApiKey() {
        if (cachedPexelsApiKey == null) {
            cachedPexelsApiKey = remoteConfig.getString("PEXELS_API_KEY");
        }
        return cachedPexelsApiKey;
    }

    /**
     * Check if Pexels API key is available
     */
    public boolean isPexelsApiKeyAvailable() {
        String apiKey = getPexelsApiKey();
        return apiKey != null && !apiKey.trim().isEmpty();
    }
}