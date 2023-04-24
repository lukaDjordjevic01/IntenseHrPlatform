package com.example.internshipIntense.service.skill;

import com.example.internshipIntense.dto.skill.SkillCreationDto;
import com.example.internshipIntense.dto.skill.SkillDto;
import com.example.internshipIntense.exception.skill.SkillNotFoundException;
import com.example.internshipIntense.model.skill.Skill;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SkillService {

    public Skill findById(Integer id) throws SkillNotFoundException;

    public List<SkillDto> findAll();

    public SkillDto addSkill(SkillCreationDto skillCreationDto);

    public void removeSkill(Integer id) throws SkillNotFoundException;

}
