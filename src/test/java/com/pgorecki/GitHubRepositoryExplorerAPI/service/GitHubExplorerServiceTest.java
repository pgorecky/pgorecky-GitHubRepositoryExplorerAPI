package com.pgorecki.GitHubRepositoryExplorerAPI.service;

import com.pgorecki.GitHubRepositoryExplorerAPI.model.Branch;
import com.pgorecki.GitHubRepositoryExplorerAPI.model.GitHubRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureWebTestClient
public class GitHubExplorerServiceTest {

    @Test
    public void testGetUserRepositories() {
        String username = "testUser";

        GitHubRepository repository = new GitHubRepository();
        repository.setName("testRepository");
        repository.setFork(false);

        GitHubRepository forkRepository = new GitHubRepository();
        forkRepository.setName("testForkRepository");
        forkRepository.setFork(true);

        WebClient.Builder webClientBuilder = mock(WebClient.Builder.class);
        WebClient webClient = mock(WebClient.class);
        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);
        Flux<GitHubRepository> flux = Flux.just(repository, forkRepository);

        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(GitHubRepository.class)).thenReturn(flux);

        GitHubExplorerService gitHubExplorerService = new GitHubExplorerService(webClientBuilder);

        Mono<List<GitHubRepository>> result = gitHubExplorerService.getUserRepositories(username);

        List<GitHubRepository> repositories = result.block();
        assertNotNull(repositories);
        assertEquals(1, repositories.size());
        assertEquals("testRepository", repositories.getFirst().getName());
        assertFalse(repositories.getFirst().isFork());
    }

    @Test
    public void testSetRepositoriesBranches() {
        String username = "testUser";

        GitHubRepository repo = new GitHubRepository();
        repo.setName("testRepository");

        Branch branch = new Branch();
        branch.setName("main");
        Branch[] branches = { branch };

        WebClient.Builder webClientBuilder = mock(WebClient.Builder.class);
        WebClient webClient = mock(WebClient.class);
        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(Branch.class)).thenReturn(Flux.fromArray(branches));

        GitHubExplorerService gitHubExplorerService = new GitHubExplorerService(webClientBuilder);
        List<GitHubRepository> repositories = List.of(repo);

        gitHubExplorerService.setRepositoriesBranches(repositories, username).block();

        assertNotNull(repo.getBranches());
        assertEquals(1, repo.getBranches().size());
        assertEquals("main", repo.getBranches().getFirst().getName());
    }

}
