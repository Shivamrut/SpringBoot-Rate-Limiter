package com.ratelimiter.rate_limiter.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.ratelimiter.rate_limiter.domain.Client;
import com.ratelimiter.rate_limiter.domain.enums.Tier;

@Repository
public class InMemoryClientRepository implements ClientRepository {

    private final Map<String,Client> clientMap;

    private void loadSeedData(){
        clientMap.put("pk_free_dev_001", 
        new Client("free-dev", "pk_free_dev_001",Tier.FREE));
        clientMap.put("pk_startup_002", 
        new Client("startup", "pk_startup_002", Tier.STANDARD));
        clientMap.put("pk_enterprise_003", 
        new Client("enterprise", "pk_enterprise_003", Tier.PREMIUM));
    }

    public InMemoryClientRepository(){
        clientMap = new ConcurrentHashMap<>();
        loadSeedData();
    }

    @Override
    public Client getClient(String apiKey) {
        if(!StringUtils.hasText(apiKey)){
            return null;
        }
        return clientMap.get(apiKey);
    }
    
}
