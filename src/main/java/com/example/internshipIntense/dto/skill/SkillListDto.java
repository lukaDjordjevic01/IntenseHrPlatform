package com.example.internshipIntense.dto.skill;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Skills list")
public class SkillListDto {

    @Schema(description = "Total count", example = "1")
    private Integer totalCount;

    private List<SkillDto> results;

    //region Constructors
    public SkillListDto(Integer totalCount, List<SkillDto> results) {
        this.totalCount = totalCount;
        this.results = results;
    }

    public SkillListDto(List<SkillDto> results) {
        this.totalCount = results.size();
        this.results = results;
    }

    public SkillListDto() {
    }
    //endregion

    //region Getters and Setters
    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<SkillDto> getResults() {
        return results;
    }

    public void setResults(List<SkillDto> results) {
        this.results = results;
    }
    //endregion
}
