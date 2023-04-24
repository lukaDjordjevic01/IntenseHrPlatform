package com.example.internshipIntense.service.candidate;

import com.example.internshipIntense.dto.candidate.JobCandidateDto;
import com.example.internshipIntense.exception.candidate.EmailAlreadyExistsException;
import com.example.internshipIntense.exception.candidate.JobCandidateAlreadyHasSkillException;
import com.example.internshipIntense.exception.candidate.JobCandidateHasNoSkillException;
import com.example.internshipIntense.exception.candidate.JobCandidateNotFoundException;
import com.example.internshipIntense.exception.skill.SkillNotFoundException;
import com.example.internshipIntense.model.candidate.JobCandidate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface JobCandidateService {

    public JobCandidate findById(Integer id) throws JobCandidateNotFoundException;

    public List<JobCandidateDto> findAll();

    public JobCandidateDto addJobCandidate(JobCandidateDto jobCandidateDto) throws EmailAlreadyExistsException, SkillNotFoundException;

    public JobCandidateDto updateJobCandidateWithSkill(Integer candidateId, Integer skillId) throws JobCandidateNotFoundException, JobCandidateAlreadyHasSkillException, SkillNotFoundException;

    public JobCandidateDto removeSkillFromJobCandidate(Integer candidateId, Integer skillId) throws JobCandidateNotFoundException, SkillNotFoundException, JobCandidateHasNoSkillException;

    public void removeJobCandidate(Integer id) throws JobCandidateNotFoundException;

    public JobCandidateDto searchJobCandidateByName(String name) throws JobCandidateNotFoundException;
    
    public List<JobCandidateDto> searchJobCandidateBySkills(List<Integer> skillIds) throws JobCandidateNotFoundException, SkillNotFoundException;
}
