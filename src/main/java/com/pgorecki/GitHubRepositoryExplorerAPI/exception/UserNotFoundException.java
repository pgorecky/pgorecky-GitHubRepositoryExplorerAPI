package com.pgorecki.GitHubRepositoryExplorerAPI.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("User: " + username + " does not exist");
    }
}
