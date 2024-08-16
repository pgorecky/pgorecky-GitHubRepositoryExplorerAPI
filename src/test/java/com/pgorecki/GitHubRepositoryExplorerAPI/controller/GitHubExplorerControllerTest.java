package com.pgorecki.GitHubRepositoryExplorerAPI.controller;

import com.pgorecki.GitHubRepositoryExplorerAPI.exception.UserNotFoundException;
import com.pgorecki.GitHubRepositoryExplorerAPI.model.GitHubRepository;
import com.pgorecki.GitHubRepositoryExplorerAPI.service.GitHubExplorerService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureWebTestClient
public class GitHubExplorerControllerTest {
    @Test
    public void testListUserRepositoriesV2() {
        String username = "testUser";
        GitHubRepository repo = new GitHubRepository();
        repo.setName("testRepo");
        repo.setFork(false);

        List<GitHubRepository> repositories = List.of(repo);
        GitHubExplorerService gitHubService = mock(GitHubExplorerService.class);
        when(gitHubService.fetchUserRepositories(username)).thenReturn(Mono.just(repositories));

        GitHubExplorerController gitHubController = new GitHubExplorerController(gitHubService);

        ResponseEntity<List<GitHubRepository>> response = gitHubController.listUserRepositories(username);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(repositories, response.getBody());
    }

    @Test
    public void testListUserRepositoriesForNonExistentUser() {
        String username = "nonExistentUser";

        GitHubExplorerService gitHubService = mock(GitHubExplorerService.class);
        when(gitHubService.fetchUserRepositories(username))
                .thenReturn(Mono.error(new UserNotFoundException(username)));

        GitHubExplorerController gitHubController = new GitHubExplorerController(gitHubService);

        Exception exception = assertThrows(UserNotFoundException.class, () -> gitHubController.listUserRepositories(username));

        assertEquals("User: " + username + " does not exist", exception.getMessage());
    }
}
