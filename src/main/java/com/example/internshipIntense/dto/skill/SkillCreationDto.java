package com.example.internshipIntense.dto.skill;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

public class SkillCreationDto {

    @Schema(description = "Skill name", example = "Java programming")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "The name must contain only letters")
    private String name;

    public SkillCreationDto(String name) {
        this.name = name;
    }

    public SkillCreationDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
