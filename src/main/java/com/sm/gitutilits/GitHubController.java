package com.sm.gitutilits;

import com.sm.gitutilits.model.BranchComparison;
import com.sm.gitutilits.model.GitBranch;
import com.sm.gitutilits.model.GitPullRequest;
import com.sm.gitutilits.model.GitRepository;
import com.sm.gitutilits.service.GitHubService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/github")
public class GitHubController {

    private final GitHubService gitHubService;

    public GitHubController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping("/repos")
    public List<GitRepository> getAllRepositories(){
        return gitHubService.listRepositories();
    }

    @GetMapping("/repos/{owner}/{repo}/pulls")
    public List<GitPullRequest> getPullRequests(
            @PathVariable String owner,
            @PathVariable String repo,
            @RequestParam(defaultValue = "open") String state
    ){
        return gitHubService.listPullRequests(owner, repo, state);
    }

    @GetMapping("/repos/{owner}/{repo}/compare/{to}/{from}")
    public BranchComparison compareBranches(
            @PathVariable String owner,
            @PathVariable String repo,
            @PathVariable String to,
            @PathVariable String from
    ){
        return gitHubService.compareBranches(owner, repo, to, from);
    }

    @GetMapping("/repos/{owner}/{repo}/branches")
    public List<GitBranch> listAllBranches(
            @PathVariable String owner,
            @PathVariable String repo
    ){
        return gitHubService.listAllBranches(owner, repo);
    }


}
