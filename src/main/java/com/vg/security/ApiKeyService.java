package com.vg.security;

import com.vg.exception.InvalidApiKeyException;
import com.vg.exception.RateLimitExceededException;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Data
class ApiKeyData {
    int count;

    Long duration;

    public ApiKeyData(int count, long duration) {
        this.count = count;
        this.duration = duration;
    }
}

@Service
public class ApiKeyService {
    private Map<String, ApiKeyData> apiKeys;

    public ApiKeyService() {
        apiKeys = new HashMap<>();
        apiKeys.put("key1", new ApiKeyData(0, System.currentTimeMillis()));
        apiKeys.put("key2", new ApiKeyData(0, System.currentTimeMillis()));
        apiKeys.put("key3", new ApiKeyData(0, System.currentTimeMillis()));
        apiKeys.put("key4", new ApiKeyData(0, System.currentTimeMillis()));
        apiKeys.put("key5", new ApiKeyData(0, System.currentTimeMillis()));
    }

    public void validateApiKey(String apiKey) throws InvalidApiKeyException, RateLimitExceededException {
        if (!apiKeys.containsKey(apiKey)) {
            throw new InvalidApiKeyException();
        }

        int count = apiKeys.get(apiKey).getCount();
        long lastRequestTime = apiKeys.get(apiKey).getDuration();
        if ( count >= 5 && System.currentTimeMillis() - lastRequestTime < TimeUnit.HOURS.toMillis(1)){
            throw new RateLimitExceededException();
        }
        apiKeys.put(apiKey, new ApiKeyData(count + 1, System.currentTimeMillis()));

    }

}
