package com.example.internshipIntense.dto.skill;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Pattern;

import java.util.Objects;

public class SkillDto {

    @Schema(description = "Skill ID", example = "123")
    @Digits(integer=10, fraction=0, message="Id must be a number")
    private Integer id;

    @Schema(description = "Skill name", example = "Java programming")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "The name must contain only letters")
    private String name;


    //region Constructors

    public SkillDto(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public SkillDto() {
    }

    //endregion

    //region Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //endregion


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkillDto skillDto = (SkillDto) o;
        return Objects.equals(id, skillDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
