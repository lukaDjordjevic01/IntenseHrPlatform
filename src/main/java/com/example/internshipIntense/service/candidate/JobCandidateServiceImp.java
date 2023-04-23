package com.example.internshipIntense.service.candidate;

import com.example.internshipIntense.dto.candidate.JobCandidateDto;
import com.example.internshipIntense.exception.candidate.EmailAlreadyExistsException;
import com.example.internshipIntense.exception.candidate.JobCandidateAlreadyHasSkillException;
import com.example.internshipIntense.exception.candidate.JobCandidateHasNoSkillException;
import com.example.internshipIntense.exception.candidate.JobCandidateNotFoundException;
import com.example.internshipIntense.exception.skill.SkillNotFoundException;
import com.example.internshipIntense.mappers.candidate.JobCandidateDtoMapper;
import com.example.internshipIntense.model.candidate.JobCandidate;
import com.example.internshipIntense.model.skill.Skill;
import com.example.internshipIntense.repository.candidate.JobCandidateRepository;
import com.example.internshipIntense.service.skill.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobCandidateServiceImp implements JobCandidateService {

    @Autowired
    private JobCandidateRepository jobCandidateRepository;

    @Autowired
    private SkillService skillService;

    @Override
    public JobCandidate findById(Integer id) throws JobCandidateNotFoundException {
        return jobCandidateRepository.findById(id).orElseThrow(() ->
                new JobCandidateNotFoundException("Candidate with this id doesn't exist"));
    }

    @Override
    public List<JobCandidateDto> findAll() {
        return jobCandidateRepository.findAll().stream().map(JobCandidateDtoMapper::fromJobCandidateToJobCandidateDto)
                .collect(Collectors.toList());
    }

    @Override
    public JobCandidateDto addJobCandidate(JobCandidateDto jobCandidateDto) throws EmailAlreadyExistsException {
        JobCandidate newCandidate = JobCandidateDtoMapper.fromJobCandidateDtoToJobCandidate(jobCandidateDto);
        if (jobCandidateRepository.findByEmail(newCandidate.getEmail()).isPresent())
            throw new EmailAlreadyExistsException("Candidate with this e-mail already exists!");
        return JobCandidateDtoMapper.fromJobCandidateToJobCandidateDto(jobCandidateRepository.save(newCandidate));
    }

    @Override
    public JobCandidateDto updateJobCandidateWithSkill(Integer candidateId, Integer skillId)
            throws JobCandidateNotFoundException, JobCandidateAlreadyHasSkillException, SkillNotFoundException {
        JobCandidate jobCandidate = findById(candidateId);
        Skill skill = skillService.findById(skillId);
        if (jobCandidate.getSkills().contains(skill))
            throw new JobCandidateAlreadyHasSkillException("Job candidate already has this skill!");
        jobCandidate.getSkills().add(skill);
        JobCandidate updatedCandidate = jobCandidateRepository.save(jobCandidate);
        return JobCandidateDtoMapper.fromJobCandidateToJobCandidateDto(updatedCandidate);
    }

    @Override
    public JobCandidateDto removeSkillFromJobCandidate(Integer candidateId, Integer skillId)
            throws JobCandidateNotFoundException, SkillNotFoundException, JobCandidateHasNoSkillException {
        JobCandidate jobCandidate = findById(candidateId);
        Skill skill = skillService.findById(skillId);
        if (!jobCandidate.getSkills().contains(skill))
            throw new JobCandidateHasNoSkillException("Candidate doesn't have given skill!");
        jobCandidate.getSkills().remove(skill);
        JobCandidate updatedCandidate = jobCandidateRepository.save(jobCandidate);
        return JobCandidateDtoMapper.fromJobCandidateToJobCandidateDto(updatedCandidate);
    }

    @Override
    public void removeJobCandidate(Integer id) throws JobCandidateNotFoundException {
        JobCandidate jobCandidate = findById(id);
        jobCandidateRepository.delete(jobCandidate);
    }

    @Override
    public JobCandidateDto searchJobCandidateByName(String name) throws JobCandidateNotFoundException {
        return JobCandidateDtoMapper.fromJobCandidateToJobCandidateDto(jobCandidateRepository
                .findByNameIgnoreCase(name).orElseThrow(()
                        -> new JobCandidateNotFoundException("Candidate with this name doesn't exist")));
    }

    @Override
    public List<JobCandidateDto> searchJobCandidateBySkills(List<Integer> skillIds)
            throws JobCandidateNotFoundException, SkillNotFoundException {

        for (Integer skillId : skillIds){
            Skill skill = skillService.findById(skillId);
        }

        List<JobCandidate> jobCandidates = jobCandidateRepository.findBySkills(skillIds, skillIds.size());
        if (jobCandidates.isEmpty())
            throw new JobCandidateNotFoundException("There are no candidates with given skills!");

        return jobCandidates.stream().map(JobCandidateDtoMapper::fromJobCandidateToJobCandidateDto)
                .collect(Collectors.toList());

    }


}
