package com.pgorecki.GitHubRepositoryExplorerAPI.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonPropertyOrder({"name", "owner", "branches"})
public class GitHubRepository {

    @JsonProperty("name")
    private String name;

    @JsonProperty("fork")
    @JsonIgnore
    private boolean fork;

    @JsonProperty("owner")
    private Owner owner;

    List<Branch> branches;

    public static class Owner {
        @JsonProperty("login")
        private String login;
    }
}
