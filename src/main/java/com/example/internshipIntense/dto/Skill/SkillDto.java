package com.example.internshipIntense.dto.Skill;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Pattern;

public class SkillDto {

    @Digits(integer=10, fraction=0, message="Id must be a number")
    private Integer id;

    @Pattern(regexp = "^[a-zA-Z]*$", message = "The name must contain only letters")
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
}
