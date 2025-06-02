package com.erendogan6.planmyworkout.core.util;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ConfigManager {

    private final FirebaseRemoteConfig remoteConfig;
    private String cachedPexelsApiKey;
    private String cachedGeminiApiKey;

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
     * Get Gemini API key with caching
     */
    public String getGeminiApiKey() {
        if (cachedGeminiApiKey == null) {
            cachedGeminiApiKey = remoteConfig.getString("GEMINI_API_KEY");
        }
        return cachedGeminiApiKey;
    }

    /**
     * Check if Pexels API key is available
     */
    public boolean isPexelsApiKeyAvailable() {
        String apiKey = getPexelsApiKey();
        return apiKey != null && !apiKey.trim().isEmpty() && !apiKey.equals("default_value");
    }

    /**
     * Check if Gemini API key is available
     */
    public boolean isGeminiApiKeyAvailable() {
        String apiKey = getGeminiApiKey();
        return apiKey != null && !apiKey.trim().isEmpty() && !apiKey.equals("default_value");
    }

    /**
     * Force refresh cache (call after remote config fetch)
     */
    public void refreshCache() {
        cachedPexelsApiKey = null;
        cachedGeminiApiKey = null;
    }
}