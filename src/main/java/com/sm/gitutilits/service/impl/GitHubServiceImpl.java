package com.sm.gitutilits.service.impl;

import com.sm.gitutilits.model.BranchComparison;
import com.sm.gitutilits.model.GitBranch;
import com.sm.gitutilits.model.GitPullRequest;
import com.sm.gitutilits.model.GitRepository;
import com.sm.gitutilits.service.GitHubService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class GitHubServiceImpl implements GitHubService {

    private final WebClient gitHubWebClient;

    public GitHubServiceImpl(WebClient webClient){
        this.gitHubWebClient = webClient;
    }

    @Override
    public List<GitRepository> listRepositories() {
        List<GitRepository> repos =  gitHubWebClient.get()
                .uri("/user/repos")
                .retrieve()
                .bodyToFlux(GitRepository.class)
                .collectList()
                .block();

        return repos.stream().filter(repo -> !repo.getName().equals("Default-Interface-Java-8")).toList();
    }

    @Override
    public List<List<GitPullRequest>> listAllPullRequests(String owner, String state) {

        List<GitRepository> repos = listRepositories();
        List<List<GitPullRequest>> pullRequests = new ArrayList<>();

        repos.stream().forEach(repo -> {
            System.out.println(repo.getName());
            pullRequests.add(listPullRequests(owner, repo.getName(), state));
        });

        return pullRequests;
    }

    @Override
    public List<GitPullRequest> listPullRequests(String owner, String repo, String state) {

        if (repo.equals("Default-Interface-Java-8")) {
            return null;
        }

        List<GitPullRequest> pullRequests =  gitHubWebClient.get()
                .uri("/repos/{owner}/{repo}/pulls?state={state}", owner, repo, state)
                .retrieve()
                .bodyToFlux(GitPullRequest.class)
                .collectList()
                .block();

        for (GitPullRequest pr : pullRequests) {
            pr.setRepo(repo);  // Set the repoName property
        }

        return pullRequests;
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
