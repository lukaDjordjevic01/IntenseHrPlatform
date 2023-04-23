package com.example.internshipIntense.dto.candidate;

import com.example.internshipIntense.dto.skill.SkillDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JobCandidateDto {

    private Integer id;

    @Pattern(regexp = "^[a-zA-Z ]*$", message = "The name must contain only letters")
    private String name;

    private LocalDate dateOfBirth;

    @Size(min = 5, max = 20)
    private String contactNumber;

    @Email
    private String email;

    private List<SkillDto> skills = new ArrayList<>();

    //region Constructors

    public JobCandidateDto(Integer id, String name, LocalDate dateOfBirth, String contactNumber, String email, List<SkillDto> skills) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.contactNumber = contactNumber;
        this.email = email;
        this.skills = skills;
    }

    public JobCandidateDto() {
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<SkillDto> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillDto> skills) {
        this.skills = skills;
    }

    //endregion
}
