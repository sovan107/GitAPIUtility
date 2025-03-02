package com.sm.gitutilits.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class BranchComparison {
    private String status;
    @JsonProperty("ahead_by")
    private Integer aheadBy;
    @JsonProperty("behind_by")
    private Integer behindBy;
    @JsonProperty("total_commits")
    private Integer totalCommits;
}
