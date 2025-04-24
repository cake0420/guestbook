package com.cake7.guestbook.common.oauth.jwtAssistant;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OAuth2ProviderStrategyFactory {
    private final Map<String, OAuth2ProviderStrategy> strategies;

    public OAuth2ProviderStrategyFactory(List<OAuth2ProviderStrategy> strategyList) {
        strategies = new HashMap<>();
        strategies.put("google", findStrategy(strategyList, GoogleOAuth2Strategy.class));
        strategies.put("naver", findStrategy(strategyList, NaverOAuth2Strategy.class));
        strategies.put("kakao", findStrategy(strategyList, KakaoOAuth2Strategy.class));
    }

    private OAuth2ProviderStrategy findStrategy(List<OAuth2ProviderStrategy> strategies,
                                                Class<? extends OAuth2ProviderStrategy> strategyClass) {
        return strategies.stream()
                .filter(strategyClass::isInstance)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No strategy found for " + strategyClass.getSimpleName()));
    }

    public OAuth2ProviderStrategy getStrategy(String registrationId) {
        OAuth2ProviderStrategy strategy = strategies.get(registrationId.toLowerCase());
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown provider: " + registrationId);
        }
        return strategy;
    }
}