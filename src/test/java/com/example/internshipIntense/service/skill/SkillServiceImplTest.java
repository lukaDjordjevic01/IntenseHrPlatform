package com.example.internshipIntense.service.skill;

import com.example.internshipIntense.InternshipIntenseApplication;
import com.example.internshipIntense.dto.skill.SkillCreationDto;
import com.example.internshipIntense.dto.skill.SkillDto;
import com.example.internshipIntense.exception.skill.SkillNotFoundException;
import com.example.internshipIntense.mappers.skill.SkillDtoMapper;
import com.example.internshipIntense.model.candidate.JobCandidate;
import com.example.internshipIntense.model.skill.Skill;
import com.example.internshipIntense.repository.candidate.JobCandidateRepository;
import com.example.internshipIntense.repository.skill.SkillRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = InternshipIntenseApplication.class)
class SkillServiceImplTest {

    @MockBean
    private SkillRepository skillRepository;

    @MockBean
    private SkillDtoMapper skillDtoMapper;

    @MockBean
    private JobCandidateRepository jobCandidateRepository;

    @Autowired
    private SkillServiceImpl skillService;

    //region Remove skill
    @Test
    @DisplayName("Should throw SkillNotFoundException when the skill id is not found")
    void removeSkillWhenSkillIdNotFoundThenThrowException() {
        Integer skillId = 1;
        when(skillRepository.findById(skillId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(SkillNotFoundException.class, () -> skillService.removeSkill(skillId));
        verify(skillRepository, times(1)).findById(skillId);
    }

    @Test
    @DisplayName("Should remove the skill and update job candidates when the skill id is valid")
    void removeSkillWhenSkillIdIsValid() {
        Integer skillId = 1;
        Skill skill = new Skill(skillId, "Java", new HashSet<>());
        JobCandidate jobCandidate =
                new JobCandidate(
                        1,
                        "John Doe",
                        LocalDate.of(1990, 1, 1),
                        "1234567890",
                        "john.doe@example.com",
                        new HashSet<>());
        jobCandidate.getSkills().add(skill);
        List<JobCandidate> jobCandidates = new ArrayList<>();
        jobCandidates.add(jobCandidate);

        when(skillRepository.findById(skillId)).thenReturn(Optional.of(skill));
        when(jobCandidateRepository.findBySkills(Collections.singletonList(skillId), 1))
                .thenReturn(jobCandidates);


        try {
            skillService.removeSkill(skillId);
        } catch (SkillNotFoundException e) {
            fail("Should not throw exception");
        }

        assertTrue(jobCandidate.getSkills().isEmpty());
        verify(skillRepository, times(1)).findById(skillId);
        verify(jobCandidateRepository, times(1))
                .findBySkills(Collections.singletonList(skillId), 1);
        verify(jobCandidateRepository, times(1)).save(jobCandidate);
        verify(skillRepository, times(1)).delete(skill);
    }
    //endregion

    //region Add skill
    @Test
    @DisplayName("Should add a new skill and return the created skill DTO")
    void addSkillAndReturnCreatedSkillDto() {
        SkillCreationDto skillCreationDto = new SkillCreationDto("Java programming");
        Skill skill = new Skill(null, "Java programming", new HashSet<>());
        Skill savedSkill = new Skill(1, "Java programming", new HashSet<>());
        SkillDto expectedSkillDto = new SkillDto(1, "Java programming");

        when(skillDtoMapper.fromSkillCreationDtoToSkill(skillCreationDto)).thenReturn(skill);
        when(skillRepository.save(skill)).thenReturn(savedSkill);
        when(skillDtoMapper.fromSkillToSkillDto(savedSkill)).thenReturn(expectedSkillDto);

        SkillDto actualSkillDto = skillService.addSkill(skillCreationDto);

        assertEquals(expectedSkillDto, actualSkillDto);
        verify(skillDtoMapper).fromSkillCreationDtoToSkill(skillCreationDto);
        verify(skillRepository).save(skill);
        verify(skillDtoMapper).fromSkillToSkillDto(savedSkill);
    }
    //endregion

    //region Find all skills
    @Test
    @DisplayName("Should return all skills as SkillDto objects")
    void findAllReturnsAllSkillsAsSkillDtoObjects() {
        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(1, "Java programming", new HashSet<>()));
        skills.add(new Skill(2, "Python programming", new HashSet<>()));
        skills.add(new Skill(3, "JavaScript programming", new HashSet<>()));

        SkillDto skillDto1 = new SkillDto(1, "Java programming");
        SkillDto skillDto2 = new SkillDto(2, "Python programming");
        SkillDto skillDto3 = new SkillDto(3, "JavaScript programming");


        when(skillRepository.findAll()).thenReturn(skills);
        when(skillDtoMapper.fromSkillToSkillDto(skills.get(0))).thenReturn(skillDto1);
        when(skillDtoMapper.fromSkillToSkillDto(skills.get(1))).thenReturn(skillDto2);
        when(skillDtoMapper.fromSkillToSkillDto(skills.get(2))).thenReturn(skillDto3);


        List<SkillDto> skillDtos = skillService.findAll();

        assertEquals(3, skillDtos.size());
        assertEquals("Java programming", skillDtos.get(0).getName());
        assertEquals("Python programming", skillDtos.get(1).getName());
        assertEquals("JavaScript programming", skillDtos.get(2).getName());

        verify(skillRepository, times(1)).findAll();
    }
    //endregion

    //region Find skill by id
    @Test
    @DisplayName("Should throw SkillNotFoundException when the id does not exist")
    void findByIdWhenIdDoesNotExistThenThrowSkillNotFoundException() {
        Integer nonExistentId = 999;
        when(skillRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(SkillNotFoundException.class, () -> skillService.findById(nonExistentId));
        verify(skillRepository, times(1)).findById(nonExistentId);
    }

    @Test
    @DisplayName("Should return the skill when the id exists")
    void findByIdWhenIdExists() {
        Integer skillId = 1;
        Skill expectedSkill = new Skill(skillId, "Java programming", new HashSet<>());
        when(skillRepository.findById(skillId)).thenReturn(Optional.of(expectedSkill));

        Skill actualSkill = null;
        try {
            actualSkill = skillService.findById(skillId);
        } catch (SkillNotFoundException e) {
            fail("Should not throw SkillNotFoundException");
        }

        assertEquals(expectedSkill, actualSkill);
        verify(skillRepository, times(1)).findById(skillId);
    }
    //endregion
}