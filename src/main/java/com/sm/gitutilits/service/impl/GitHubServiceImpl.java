package com.sm.gitutilits.service.impl;

import com.sm.gitutilits.model.BranchComparison;
import com.sm.gitutilits.model.GitBranch;
import com.sm.gitutilits.model.GitPullRequest;
import com.sm.gitutilits.model.GitRepository;
import com.sm.gitutilits.service.GitHubService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class GitHubServiceImpl implements GitHubService {

    private final WebClient gitHubWebClient;

    public GitHubServiceImpl(WebClient webClient){
        this.gitHubWebClient = webClient;
    }

    @Override
    public List<GitRepository> listRepositories() {
        return gitHubWebClient.get()
                .uri("/user/repos")
                .retrieve()
                .bodyToFlux(GitRepository.class)
                .collectList()
                .block();
    }

    @Override
    public List<GitPullRequest> listPullRequests(String owner, String repo, String state) {
        return gitHubWebClient.get()
                .uri("/repos/{owner}/{repo}/pulls?state={state}", owner, repo, state)
                .retrieve()
                .bodyToFlux(GitPullRequest.class)
                .collectList()
                .block();
    }

    @Override
    public BranchComparison compareBranches(String owner, String repo, String to, String from) {
        return gitHubWebClient.get()
                .uri("/repos/{owner}/{repo}/compare/{to}...{from}", owner, repo, to, from)
                .retrieve()
                .bodyToMono(BranchComparison.class)
                .block();
    }

    @Override
    public List<GitBranch> listAllBranches(String owner, String repo) {
        return gitHubWebClient.get()
                .uri("/repos/{owner}/{repo}/branches", owner, repo)
                .retrieve()
                .bodyToFlux(GitBranch.class)
                .collectList()
                .block();
    }
}
