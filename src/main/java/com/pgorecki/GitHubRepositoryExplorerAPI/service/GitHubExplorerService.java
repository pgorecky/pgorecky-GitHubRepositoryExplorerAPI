package com.pgorecki.GitHubRepositoryExplorerAPI.service;

import com.pgorecki.GitHubRepositoryExplorerAPI.exception.UserNotFoundException;
import com.pgorecki.GitHubRepositoryExplorerAPI.model.Branch;
import com.pgorecki.GitHubRepositoryExplorerAPI.model.GitHubRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GitHubExplorerService {

    private final WebClient webClient;
    public static final String BASE_URL = "https://api.github.com";

    public GitHubExplorerService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
    }

    public Mono<List<GitHubRepository>> getUserRepositories(String username) {
        return webClient.get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .bodyToFlux(GitHubRepository.class)
                .filter(repository -> !repository.isFork())
                .collectList()
                .onErrorResume(WebClientResponseException.NotFound.class, e ->
                        Mono.error(new UserNotFoundException(username))
                );
    }

    public Mono<Void> setRepositoriesBranches(List<GitHubRepository> repositories, String username) {
        return Flux.fromIterable(repositories)
                .flatMap(repository -> webClient.get()
                        .uri("/repos/{username}/{repository}/branches", username, repository.getName())
                        .retrieve()
                        .bodyToFlux(Branch.class)
                        .collectList()
                        .doOnNext(repository::setBranches)
                )
                .then();
    }

    public Mono<List<GitHubRepository>> fetchUserRepositories(String username) {
        return getUserRepositories(username)
                .flatMap(repositories -> setRepositoriesBranches(repositories, username)
                        .thenReturn(repositories));
    }
}
