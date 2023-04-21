package com.example.internshipIntense.mappers.skill;

import com.example.internshipIntense.dto.skill.SkillDto;
import com.example.internshipIntense.model.skill.Skill;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SkillDtoMapper {

    private static ModelMapper modelMapper;

    @Autowired
    public SkillDtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper; //potentially remove this line
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public static SkillDto fromSkillToSkillDto(Skill skill){
        return modelMapper.map(skill, SkillDto.class);
    }

    public static Skill fromSkillDtoToSkill(SkillDto skillDto){
        return modelMapper.map(skillDto, Skill.class);
    }


}
