package com.sm.gitutilits.service;

import com.sm.gitutilits.model.BranchComparison;
import com.sm.gitutilits.model.GitBranch;
import com.sm.gitutilits.model.GitPullRequest;
import com.sm.gitutilits.model.GitRepository;

import java.util.List;

public interface GitHubService {
    public List<GitRepository> listRepositories();

    List<GitPullRequest> listPullRequests(String owner, String repo, String state);

    BranchComparison compareBranches(String owner, String repo, String to, String from);

    List<GitBranch> listAllBranches(String owner, String repo);

    List<List<GitPullRequest>> listAllPullRequests(String owner, String state);
}
