package com.example.internshipIntense.model.candidate;

import com.example.internshipIntense.model.skill.Skill;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "job_candidates")
public class JobCandidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "full_name")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "The name must contain only letters")
    private String name;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "contact_number")
    @Size(min = 5, max = 20)
    private String contactNumber;

    @Column(name = "email")
    @Email
    private String email;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "job_candidate_skill", joinColumns = {
            @JoinColumn(name = "job_candidate_id", referencedColumnName = "id") }, inverseJoinColumns = {
            @JoinColumn(name = "skill_id", referencedColumnName = "id") })
    private Set<Skill> skills = new HashSet<>();

    //region Constructors

    public JobCandidate(Integer id, String name, LocalDate dateOfBirth, String contactNumber, String email, Set<Skill> skills) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.contactNumber = contactNumber;
        this.email = email;
        this.skills = skills;
    }

    public JobCandidate() {
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

    public Set<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }
    //endregion



}
