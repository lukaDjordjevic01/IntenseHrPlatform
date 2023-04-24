package com.example.internshipIntense.dto.candidate;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Job candidates list")
public class JobCandidateListDto {

    @Schema(description = "Total count", example = "1")
    private Integer totalCount;
    private List<JobCandidateDto> results;

    //region Constructors
    public JobCandidateListDto(Integer totalCount, List<JobCandidateDto> results) {
        this.totalCount = totalCount;
        this.results = results;
    }

    public JobCandidateListDto(List<JobCandidateDto> results) {
        this.totalCount = results.size();
        this.results = results;
    }

    public JobCandidateListDto() {
    }
    //endregion

    //region Getters and Setters
    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<JobCandidateDto> getResults() {
        return results;
    }

    public void setResults(List<JobCandidateDto> results) {
        this.results = results;
    }
    //endregion
}
