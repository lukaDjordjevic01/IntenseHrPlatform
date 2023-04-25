package com.example.internshipIntense.dto.skill;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkillCreationDto that = (SkillCreationDto) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
