package com.example.internshipIntense.mappers.candidate;

import com.example.internshipIntense.dto.candidate.JobCandidateDto;
import com.example.internshipIntense.mappers.skill.SkillDtoMapper;
import com.example.internshipIntense.model.candidate.JobCandidate;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class JobCandidateDtoMapper {

    private final ModelMapper modelMapper;

    @Autowired
    private SkillDtoMapper skillDtoMapper;

    @Autowired
    public JobCandidateDtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper; //potentially remove this line
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public JobCandidateDto fromJobCandidateToJobCandidateDto(JobCandidate jobCandidate){
        JobCandidateDto jobCandidateDto = modelMapper.map(jobCandidate, JobCandidateDto.class);
        jobCandidateDto.setSkills(jobCandidate.getSkills().stream()
                .map(skill -> skillDtoMapper.fromSkillToSkillDto(skill)).collect(Collectors.toList()));
        return jobCandidateDto;
    }

    public JobCandidate fromJobCandidateDtoToJobCandidate(JobCandidateDto jobCandidateDto){
        return modelMapper.map(jobCandidateDto, JobCandidate.class);
    }
}
