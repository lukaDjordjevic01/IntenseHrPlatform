package com.example.internshipIntense.model.skill;

import com.example.internshipIntense.model.candidate.JobCandidate;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "skills")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "skill_name")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "The name must contain only letters")
    private String name;

    @ManyToMany(mappedBy = "skills", fetch = FetchType.LAZY)
    private Set<JobCandidate> jobCandidates = new HashSet<>();


    //region Constructors

    public Skill(Integer id, String name, Set<JobCandidate> jobCandidates) {
        this.id = id;
        this.name = name;
        this.jobCandidates = jobCandidates;
    }

    public Skill() {
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

    public Set<JobCandidate> getJobCandidates() {
        return jobCandidates;
    }

    public void setJobCandidates(Set<JobCandidate> jobCandidates) {
        this.jobCandidates = jobCandidates;
    }

    //endregion
}
