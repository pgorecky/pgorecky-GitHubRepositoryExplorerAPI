package com.pgorecki.GitHubRepositoryExplorerAPI.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Branch {

    @JsonProperty("name")
    private String name;
    @JsonProperty("commit")
    private Commit commit;

    public static class Commit {
        @JsonProperty("sha")
        String sha;
    }
}
