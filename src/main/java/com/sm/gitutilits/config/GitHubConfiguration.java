package com.sm.gitutilits.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GitHubConfiguration {
    @Value("${github.api.url}")
    private String githubApiUrl;

    @Value("${github.api.token}")
    private String githubToken;

    @Bean
    public WebClient gitHubWebClient(){
        return WebClient.builder()
                .baseUrl(githubApiUrl)
                .defaultHeader("Authorization" ,"Bearer " + githubToken)
                .defaultHeader("Accept",  "application/vnd.github+json")
                .defaultHeader("X-GitHub-Api-Version",  "2022-11-28")
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        return mapper;
    }

}
