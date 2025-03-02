package com.sm.gitutilits.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GitRepository {
    private Long id;
    private String name;
    @JsonProperty("full_name")
    private String fullName;
    private String description;
    private boolean isPrivate;
    @JsonProperty("html_url")
    private String htmlUrl;
}
