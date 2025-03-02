package com.sm.gitutilits.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GitPullRequest {

    private Long id;
    private Long number;
    private String title;
    private String state;
    private String body;
    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String createdAt;
    @JsonProperty("updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String updatedAt;
    private BranchType head;    // from branch
    private BranchType base;    // to Branch
}
