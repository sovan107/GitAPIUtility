package com.sm.gitutilits.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GitBranch {

    private String name;

    @JsonProperty("protected")
    private boolean isProtected;
}
