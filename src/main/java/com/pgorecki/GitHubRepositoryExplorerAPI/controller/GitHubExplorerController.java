package com.pgorecki.GitHubRepositoryExplorerAPI.controller;

import com.pgorecki.GitHubRepositoryExplorerAPI.model.GitHubRepository;
import com.pgorecki.GitHubRepositoryExplorerAPI.service.GitHubExplorerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/github")
public class GitHubExplorerController {

    private final GitHubExplorerService gitHubExplorerService;

    @GetMapping("/users/{username}")
    public ResponseEntity<List<GitHubRepository>> listUserRepositoriesV2(@PathVariable String username) {
        log.info("Fetching repositories for user: {}", username);

        return ResponseEntity.ok(gitHubExplorerService.fetchUserRepositories(username).block());
    }
}
