package com.example.internshipIntense.service.skill;

import com.example.internshipIntense.dto.skill.SkillCreationDto;
import com.example.internshipIntense.dto.skill.SkillDto;
import com.example.internshipIntense.exception.skill.SkillNotFoundException;
import com.example.internshipIntense.mappers.skill.SkillDtoMapper;
import com.example.internshipIntense.model.candidate.JobCandidate;
import com.example.internshipIntense.model.skill.Skill;
import com.example.internshipIntense.repository.candidate.JobCandidateRepository;
import com.example.internshipIntense.repository.skill.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkillServiceImpl implements SkillService {

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private JobCandidateRepository jobCandidateRepository;

    @Autowired
    private SkillDtoMapper skillDtoMapper;

    @Override
    public Skill findById(Integer id) throws SkillNotFoundException {
        return skillRepository.findById(id).orElseThrow(()
                -> new SkillNotFoundException("Skill with this id doesn't exist!"));
    }

    @Override
    public List<SkillDto> findAll() {
        return skillRepository.findAll().stream()
                .map(skill -> skillDtoMapper.fromSkillToSkillDto(skill)).collect(Collectors.toList());

    }

    @Override
    public SkillDto addSkill(SkillCreationDto skillCreationDto) {
        Skill skill = skillDtoMapper.fromSkillCreationDtoToSkill(skillCreationDto);
        return skillDtoMapper.fromSkillToSkillDto(skillRepository.save(skill));
    }

    @Override
    public void removeSkill(Integer id) throws SkillNotFoundException {
        Skill skill = findById(id);
        List<Integer> skillIds = Collections.singletonList(id);
        for (JobCandidate jobCandidate : jobCandidateRepository.findBySkills(skillIds, 1)){
            jobCandidate.getSkills().remove(skill);
            jobCandidateRepository.save(jobCandidate);
        }

        skillRepository.delete(skill);
    }

}
