package com.example.internshipIntense.service.candidate;

import com.example.internshipIntense.InternshipIntenseApplication;
import com.example.internshipIntense.dto.candidate.JobCandidateDto;
import com.example.internshipIntense.dto.skill.SkillDto;
import com.example.internshipIntense.exception.candidate.EmailAlreadyExistsException;
import com.example.internshipIntense.exception.candidate.JobCandidateAlreadyHasSkillException;
import com.example.internshipIntense.exception.candidate.JobCandidateHasNoSkillException;
import com.example.internshipIntense.exception.candidate.JobCandidateNotFoundException;
import com.example.internshipIntense.exception.skill.SkillNotFoundException;
import com.example.internshipIntense.mappers.candidate.JobCandidateDtoMapper;
import com.example.internshipIntense.model.candidate.JobCandidate;
import com.example.internshipIntense.model.skill.Skill;
import com.example.internshipIntense.repository.candidate.JobCandidateRepository;
import com.example.internshipIntense.repository.skill.SkillRepository;
import com.example.internshipIntense.service.skill.SkillServiceImpl;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = InternshipIntenseApplication.class)
class JobCandidateServiceImpTest {

    @MockBean
    private JobCandidateRepository jobCandidateRepository;

    @MockBean
    private SkillServiceImpl skillService;

    @MockBean
    private JobCandidateDtoMapper jobCandidateDtoMapper;

    @Autowired
    private JobCandidateServiceImp jobCandidateService;

    //region Remove skill from job candidate
    @Test
    @DisplayName("Should throw an exception when the job candidate is not found")
    void removeSkillFromJobCandidateWhenCandidateNotFoundThenThrowException() {
        when(jobCandidateRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(
                JobCandidateNotFoundException.class,
                () -> jobCandidateService.removeSkillFromJobCandidate(1, 1));
    }

    @Test
    @DisplayName("Should throw an exception when the skill is not found")
    void removeSkillFromJobCandidateWhenSkillNotFoundThenThrowException() throws SkillNotFoundException {
        Integer candidateId = 1;
        Integer skillId = 2;

        JobCandidate jobCandidate =
                new JobCandidate(
                        candidateId,
                        "John Smith",
                        LocalDate.of(1990, 1, 1),
                        "+123456789",
                        "johnsmith@gmail.com",
                        new HashSet<>());
        Skill skill = new Skill(skillId, "Java", new HashSet<>());

        when(jobCandidateRepository.findById(candidateId)).thenReturn(Optional.of(jobCandidate));
        when(skillService.findById(skillId))
                .thenThrow(SkillNotFoundException.class);

        // Act and Assert
        assertThrows(
                SkillNotFoundException.class,
                () -> jobCandidateService.removeSkillFromJobCandidate(candidateId, skillId));
        verify(jobCandidateRepository, times(1)).findById(candidateId);
        verify(skillService, times(1)).findById(skillId);
    }

    @Test
    @DisplayName("Should throw an exception when the job candidate does not have the skill")
    void removeSkillFromJobCandidateWhenCandidateDoesNotHaveSkillThenThrowException() throws SkillNotFoundException {
        Integer candidateId = 1;
        Integer skillId = 1;
        JobCandidate jobCandidate =
                new JobCandidate(
                        candidateId,
                        "John Doe",
                        LocalDate.of(1990, 1, 1),
                        "+1234567890",
                        "john.doe@example.com",
                        new HashSet<>());
        Skill skill = new Skill(skillId, "Java", new HashSet<>());

        when(jobCandidateRepository.findById(candidateId)).thenReturn(Optional.of(jobCandidate));
        when(skillService.findById(skillId)).thenReturn(skill);

        // Act and Assert
        assertThrows(
                JobCandidateHasNoSkillException.class,
                () -> jobCandidateService.removeSkillFromJobCandidate(candidateId, skillId));
        verify(jobCandidateRepository, times(1)).findById(candidateId);
        verify(skillService, times(1)).findById(skillId);
    }

    @Test
    @DisplayName("Should remove the skill from the job candidate when the candidate has the skill")
    void removeSkillFromJobCandidateWhenCandidateHasSkill() throws SkillNotFoundException {
        Integer candidateId = 1;
        Integer skillId = 1;
        JobCandidate jobCandidate =
                new JobCandidate(
                        candidateId,
                        "John Smith",
                        LocalDate.of(1990, 1, 1),
                        "+123456789",
                        "johnsmith@gmail.com",
                        new HashSet<>());
        Skill skill = new Skill(skillId, "Java", new HashSet<>());
        jobCandidate.getSkills().add(skill);

        JobCandidate updatedJobCandidate =
                new JobCandidate(
                        candidateId,
                        "John Smith",
                        LocalDate.of(1990, 1, 1),
                        "+123456789",
                        "johnsmith@gmail.com",
                        new HashSet<>());
        JobCandidateDto updatedJobCandidateDto =
                new JobCandidateDto(
                        candidateId,
                        "John Smith",
                        LocalDate.of(1990, 1, 1),
                        "+123456789",
                        "johnsmith@gmail.com",
                        new ArrayList<>());

        when(jobCandidateRepository.findById(candidateId)).thenReturn(Optional.of(jobCandidate));
        when(skillService.findById(skillId)).thenReturn(skill);
        when(jobCandidateRepository.save(any(JobCandidate.class))).thenReturn(updatedJobCandidate);
        when(jobCandidateDtoMapper.fromJobCandidateToJobCandidateDto(updatedJobCandidate))
                .thenReturn(updatedJobCandidateDto);

        JobCandidateDto result = null;
        try {
            result = jobCandidateService.removeSkillFromJobCandidate(candidateId, skillId);
        } catch (JobCandidateNotFoundException
                 | SkillNotFoundException
                 | JobCandidateHasNoSkillException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertNotNull(result);
        assertEquals(candidateId, result.getId());
        assertFalse(result.getSkills().contains(new SkillDto(skillId, "Java")));
        verify(jobCandidateRepository, times(1)).findById(candidateId);
        verify(skillService, times(1)).findById(skillId);
        verify(jobCandidateRepository, times(1)).save(any(JobCandidate.class));
        verify(jobCandidateDtoMapper, times(1))
                .fromJobCandidateToJobCandidateDto(updatedJobCandidate);
    }
    //endregion

    //region Update job candidate with skill
    @Test
    @DisplayName("Should throw an exception when the job candidate is not found")
    void updateJobCandidateWithSkillWhenCandidateNotFoundThenThrowException() {
        when(jobCandidateRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(
                JobCandidateNotFoundException.class,
                () -> jobCandidateService.updateJobCandidateWithSkill(1, 1));
    }

    @Test
    @DisplayName("Should throw an exception when the skill is not found")
    void updateJobCandidateWithSkillWhenSkillNotFoundThenThrowException() throws SkillNotFoundException {
        Integer candidateId = 1;
        Integer skillId = 2;
        JobCandidate jobCandidate =
                new JobCandidate(
                        candidateId,
                        "John Smith",
                        LocalDate.of(1990, 1, 1),
                        "+123456789",
                        "johnsmith@gmail.com",
                        new HashSet<>());

        when(jobCandidateRepository.findById(candidateId)).thenReturn(Optional.of(jobCandidate));
        when(skillService.findById(skillId))
                .thenThrow(new SkillNotFoundException("Skill not found"));

        // Act and Assert
        assertThrows(
                SkillNotFoundException.class,
                () -> jobCandidateService.updateJobCandidateWithSkill(candidateId, skillId));
        verify(jobCandidateRepository).findById(candidateId);
        verify(skillService).findById(skillId);
    }

    @Test
    @DisplayName("Should throw an exception when the job candidate already has the skill")
    void updateJobCandidateWithSkillWhenSkillIsAlreadyPresentThenThrowException() throws SkillNotFoundException {
        Integer candidateId = 1;
        Integer skillId = 1;
        JobCandidate jobCandidate =
                new JobCandidate(
                        candidateId,
                        "John Smith",
                        LocalDate.of(1990, 1, 1),
                        "+123456789",
                        "johnsmith@gmail.com",
                        new HashSet<>());
        Skill skill = new Skill(skillId, "Java", new HashSet<>());
        jobCandidate.getSkills().add(skill);

        when(jobCandidateRepository.findById(candidateId)).thenReturn(Optional.of(jobCandidate));
        when(skillService.findById(skillId)).thenReturn(skill);

        // Act and Assert
        assertThrows(
                JobCandidateAlreadyHasSkillException.class,
                () -> jobCandidateService.updateJobCandidateWithSkill(candidateId, skillId));
        verify(jobCandidateRepository, times(0)).save(any(JobCandidate.class));
    }

    @Test
    @DisplayName(
            "Should update the job candidate with a new skill when the skill is not already present")
    void updateJobCandidateWithSkillWhenSkillIsNotPresent() throws SkillNotFoundException {
        Integer candidateId = 1;
        Integer skillId = 1;
        JobCandidate jobCandidate =
                new JobCandidate(
                        candidateId,
                        "John Doe",
                        LocalDate.of(1990, 1, 1),
                        "+1234567890",
                        "john.doe@example.com",
                        new HashSet<>());
        Skill skill = new Skill(skillId, "Java", new HashSet<>());
        JobCandidateDto jobCandidateDtoWithSkill =
                new JobCandidateDto(
                        candidateId,
                        "John Doe",
                        LocalDate.of(1990, 1, 1),
                        "+1234567890",
                        "john.doe@example.com",
                        new ArrayList<>(Collections.singletonList(new SkillDto(skillId, skill.getName()))));

        when(jobCandidateRepository.findById(candidateId)).thenReturn(Optional.of(jobCandidate));
        when(skillService.findById(skillId)).thenReturn(skill);
        when(jobCandidateRepository.save(any(JobCandidate.class))).thenReturn(jobCandidate);
        when(jobCandidateDtoMapper.fromJobCandidateToJobCandidateDto(any(JobCandidate.class)))
                .thenReturn(jobCandidateDtoWithSkill);

        JobCandidateDto updatedJobCandidateDto = null;
        try {
            updatedJobCandidateDto =
                    jobCandidateService.updateJobCandidateWithSkill(candidateId, skillId);
        } catch (JobCandidateNotFoundException
                 | JobCandidateAlreadyHasSkillException
                 | SkillNotFoundException e) {
            fail("Exception should not be thrown");
        }

        assertNotNull(updatedJobCandidateDto);
        assertEquals(candidateId, updatedJobCandidateDto.getId());
        assertTrue(
                updatedJobCandidateDto.getSkills().stream()
                        .anyMatch(skillDto -> skillDto.getId().equals(skillId)));

        verify(jobCandidateRepository, times(1)).findById(candidateId);
        verify(skillService, times(1)).findById(skillId);
        verify(jobCandidateRepository, times(1)).save(any(JobCandidate.class));
        verify(jobCandidateDtoMapper, times(1))
                .fromJobCandidateToJobCandidateDto(any(JobCandidate.class));
    }
    //endregion

    //region Search job candidates by skills
    @Test
    @DisplayName("Should throw an exception when no job candidates have the given skills")
    void searchJobCandidateBySkillsWhenNoCandidatesHaveGivenSkillsThenThrowException() throws SkillNotFoundException {
        List<Integer> skillIds = new ArrayList<>();
        skillIds.add(1);
        skillIds.add(2);

        when(skillService.findById(anyInt())).thenReturn(new Skill());
        when(jobCandidateRepository.findBySkills(skillIds, skillIds.size()))
                .thenReturn(new ArrayList<>());

        // Act and Assert
        assertThrows(
                JobCandidateNotFoundException.class,
                () -> jobCandidateService.searchJobCandidateBySkills(skillIds));
    }

    @Test
    @DisplayName("Should throw an exception when a skill ID is not found")
    void searchJobCandidateBySkillsWhenSkillIdNotFoundThenThrowException() throws SkillNotFoundException {
        List<Integer> skillIds = new ArrayList<>();
        skillIds.add(1);
        skillIds.add(2);

        when(skillService.findById(anyInt())).thenThrow(SkillNotFoundException.class);

        assertThrows(
                SkillNotFoundException.class,
                () -> jobCandidateService.searchJobCandidateBySkills(skillIds));

        verify(skillService, times(1)).findById(anyInt());
    }

    @Test
    @DisplayName("Should return a list of job candidates with the given skills")
    void searchJobCandidateBySkillsWithMatchingSkills() throws SkillNotFoundException {
        List<Integer> skillIds = new ArrayList<>();
        skillIds.add(1);
        skillIds.add(2);

        JobCandidate jobCandidate1 =
                new JobCandidate(
                        1,
                        "John Smith",
                        LocalDate.of(1990, 1, 1),
                        "+123456789",
                        "johnsmith@gmail.com",
                        new HashSet<>());
        JobCandidate jobCandidate2 =
                new JobCandidate(
                        2,
                        "Jane Doe",
                        LocalDate.of(1992, 2, 2),
                        "+987654321",
                        "janedoe@gmail.com",
                        new HashSet<>());

        List<JobCandidate> jobCandidates = new ArrayList<>();
        jobCandidates.add(jobCandidate1);
        jobCandidates.add(jobCandidate2);

        JobCandidateDto jobCandidateDto1 =
                new JobCandidateDto(
                        1,
                        "John Smith",
                        LocalDate.of(1990, 1, 1),
                        "+123456789",
                        "johnsmith@gmail.com",
                        new ArrayList<>());
        JobCandidateDto jobCandidateDto2 =
                new JobCandidateDto(
                        2,
                        "Jane Doe",
                        LocalDate.of(1992, 2, 2),
                        "+987654321",
                        "janedoe@gmail.com",
                        new ArrayList<>());

        List<JobCandidateDto> jobCandidateDtos = new ArrayList<>();
        jobCandidateDtos.add(jobCandidateDto1);
        jobCandidateDtos.add(jobCandidateDto2);

        when(skillService.findById(anyInt())).thenReturn(new Skill());
        when(jobCandidateRepository.findBySkills(skillIds, skillIds.size()))
                .thenReturn(jobCandidates);
        when(jobCandidateDtoMapper.fromJobCandidateToJobCandidateDto(jobCandidate1))
                .thenReturn(jobCandidateDto1);
        when(jobCandidateDtoMapper.fromJobCandidateToJobCandidateDto(jobCandidate2))
                .thenReturn(jobCandidateDto2);

        List<JobCandidateDto> result = null;
        try {
            result = jobCandidateService.searchJobCandidateBySkills(skillIds);
        } catch (JobCandidateNotFoundException e) {
            fail("Should not throw exception");
        }

        assertEquals(jobCandidateDtos.size(), result.size());
        assertEquals(jobCandidateDtos.get(0).getId(), result.get(0).getId());
        assertEquals(jobCandidateDtos.get(1).getId(), result.get(1).getId());
        verify(skillService, times(2)).findById(anyInt());
        verify(jobCandidateRepository).findBySkills(skillIds, skillIds.size());
        verify(jobCandidateDtoMapper, times(2))
                .fromJobCandidateToJobCandidateDto(any(JobCandidate.class));
    }
    // endregion

    //region Search job candidate by name
    @Test
    @DisplayName("Should throw an exception when the name does not exist")
    void searchJobCandidateByNameWhenNameDoesNotExistThenThrowException() {
        String nonExistentName = "Non Existent Name";
        when(jobCandidateRepository.findByNameIgnoreCase(nonExistentName))
                .thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(
                JobCandidateNotFoundException.class,
                () -> jobCandidateService.searchJobCandidateByName(nonExistentName));
        verify(jobCandidateRepository, times(1)).findByNameIgnoreCase(nonExistentName);
    }

    @Test
    @DisplayName("Should return the job candidate when the name exists")
    void searchJobCandidateByNameWhenNameExists() {
        String candidateName = "Jon Smith";
        JobCandidate jobCandidate =
                new JobCandidate(
                        1,
                        candidateName,
                        LocalDate.of(1999, 1, 1),
                        "+38169125365",
                        "johnsmith@gmail.com",
                        new HashSet<>());
        JobCandidateDto jobCandidateDto =
                new JobCandidateDto(
                        1,
                        candidateName,
                        LocalDate.of(1999, 1, 1),
                        "+38169125365",
                        "johnsmith@gmail.com",
                        new ArrayList<>());

        when(jobCandidateRepository.findByNameIgnoreCase(candidateName))
                .thenReturn(Optional.of(jobCandidate));
        when(jobCandidateDtoMapper.fromJobCandidateToJobCandidateDto(jobCandidate))
                .thenReturn(jobCandidateDto);

        JobCandidateDto foundJobCandidateDto = null;
        try {
            foundJobCandidateDto = jobCandidateService.searchJobCandidateByName(candidateName);
        } catch (JobCandidateNotFoundException e) {
            fail("Exception should not be thrown");
        }

        assertNotNull(foundJobCandidateDto);
        assertEquals(candidateName, foundJobCandidateDto.getName());
        verify(jobCandidateRepository, times(1)).findByNameIgnoreCase(candidateName);
        verify(jobCandidateDtoMapper, times(1)).fromJobCandidateToJobCandidateDto(jobCandidate);
    }
    // endregion

    //region Remove job candidate
    @Test
    @DisplayName("Should throw an exception when the id is not found")
    void removeJobCandidateWhenIdNotFoundThenThrowException() {
        when(jobCandidateRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(
                JobCandidateNotFoundException.class,
                () -> jobCandidateService.removeJobCandidate(1));

        verify(jobCandidateRepository, times(1)).findById(1);
        verify(jobCandidateRepository, times(0)).delete(any(JobCandidate.class));
    }

    @Test
    @DisplayName("Should remove the job candidate when the id is valid")
    void removeJobCandidateWhenIdIsValid() {
        Integer candidateId = 1;
        JobCandidate jobCandidate =
                new JobCandidate(
                        candidateId,
                        "John Doe",
                        LocalDate.of(1990, 1, 1),
                        "1234567890",
                        "john.doe@example.com",
                        new HashSet<>());
        when(jobCandidateRepository.findById(candidateId)).thenReturn(Optional.of(jobCandidate));

        assertDoesNotThrow(() -> jobCandidateService.removeJobCandidate(candidateId));

        verify(jobCandidateRepository, times(1)).findById(candidateId);
        verify(jobCandidateRepository, times(1)).delete(jobCandidate);
    }
    // endregion

    //region Add job candidate
    @Test
    @DisplayName("Should throw an exception when adding a job candidate with non-existing skills")
    void addJobCandidateWithNonExistingSkillsThrowsException() throws SkillNotFoundException {
        JobCandidateDto jobCandidateDto =
                new JobCandidateDto(
                        1,
                        "John Smith",
                        LocalDate.of(1999, 1, 1),
                        "+38169125365",
                        "johnsmith@gmail.com",
                        new ArrayList<>(Collections.singletonList(new SkillDto(14, "test"))));
        JobCandidate jobCandidate =
                new JobCandidate(
                        1,
                        "John Smith",
                        LocalDate.of(1999, 1, 1),
                        "+38169125365",
                        "johnsmith@gmail.com",
                        new HashSet<>(Collections.singletonList(new Skill(14, "test", null))));

        when(jobCandidateDtoMapper.fromJobCandidateDtoToJobCandidate(jobCandidateDto))
                .thenReturn(jobCandidate);
        when(jobCandidateRepository.findByEmail(jobCandidate.getEmail()))
                .thenReturn(Optional.empty());
        when(skillService.findById(anyInt())).thenThrow(SkillNotFoundException.class);

        // Act and Assert
        assertThrows(
                SkillNotFoundException.class,
                () -> jobCandidateService.addJobCandidate(jobCandidateDto));

        // Verify
        verify(jobCandidateDtoMapper).fromJobCandidateDtoToJobCandidate(jobCandidateDto);
        verify(jobCandidateRepository).findByEmail(jobCandidate.getEmail());
        verify(skillService, atLeastOnce()).findById(anyInt());
    }

    @Test
    @DisplayName("Should throw an exception when adding a job candidate with an existing email")
    void addJobCandidateWithExistingEmailThrowsException() {
        JobCandidateDto jobCandidateDto =
                new JobCandidateDto(
                        1,
                        "John Smith",
                        LocalDate.of(1999, 1, 1),
                        "+38169125365",
                        "johnsmith@gmail.com",
                        new ArrayList<>());
        JobCandidate jobCandidate =
                new JobCandidate(
                        1,
                        "John Smith",
                        LocalDate.of(1999, 1, 1),
                        "+38169125365",
                        "johnsmith@gmail.com",
                        new HashSet<>());

        when(jobCandidateDtoMapper.fromJobCandidateDtoToJobCandidate(jobCandidateDto))
                .thenReturn(jobCandidate);
        when(jobCandidateRepository.findByEmail(jobCandidate.getEmail()))
                .thenReturn(Optional.of(jobCandidate));

        // Act and Assert
        assertThrows(
                EmailAlreadyExistsException.class,
                () -> jobCandidateService.addJobCandidate(jobCandidateDto));

        // Verify
        verify(jobCandidateDtoMapper, times(1)).fromJobCandidateDtoToJobCandidate(jobCandidateDto);
        verify(jobCandidateRepository, times(1)).findByEmail(jobCandidate.getEmail());
        verifyNoMoreInteractions(jobCandidateRepository);
        verifyNoInteractions(skillService);
    }

    @Test
    @DisplayName("Should add a job candidate with valid email and skills")
    void addJobCandidateWithValidEmailAndSkills() {
        JobCandidateDto jobCandidateDto =
                new JobCandidateDto(
                        1,
                        "John Smith",
                        LocalDate.of(1990, 1, 1),
                        "+1234567890",
                        "johnsmith@gmail.com",
                        new ArrayList<>());
        JobCandidate jobCandidate =
                new JobCandidate(
                        1,
                        "John Smith",
                        LocalDate.of(1990, 1, 1),
                        "+1234567890",
                        "johnsmith@gmail.com",
                        new HashSet<>());

        when(jobCandidateRepository.findByEmail(jobCandidate.getEmail()))
                .thenReturn(Optional.empty());
        when(jobCandidateDtoMapper.fromJobCandidateDtoToJobCandidate(jobCandidateDto))
                .thenReturn(jobCandidate);
        when(jobCandidateRepository.save(jobCandidate)).thenReturn(jobCandidate);
        when(jobCandidateDtoMapper.fromJobCandidateToJobCandidateDto(jobCandidate))
                .thenReturn(jobCandidateDto);

        JobCandidateDto addedJobCandidateDto = null;
        try {
            addedJobCandidateDto = jobCandidateService.addJobCandidate(jobCandidateDto);
        } catch (EmailAlreadyExistsException | SkillNotFoundException e) {
            fail("Exception should not be thrown");
        }

        assertEquals(jobCandidateDto, addedJobCandidateDto);
        verify(jobCandidateRepository, times(1)).findByEmail(jobCandidate.getEmail());
        verify(jobCandidateDtoMapper, times(1)).fromJobCandidateDtoToJobCandidate(jobCandidateDto);
        verify(jobCandidateRepository, times(1)).save(jobCandidate);
        verify(jobCandidateDtoMapper, times(1)).fromJobCandidateToJobCandidateDto(jobCandidate);
    }
    // endregion

    //region Find all job candidates
    @Test
    @DisplayName("Should return a list of all JobCandidateDto objects")
    void findAllJobCandidates() {
        JobCandidate jobCandidate1 =
                new JobCandidate(
                        1,
                        "John Doe",
                        LocalDate.of(1990, 1, 1),
                        "1234567890",
                        "john.doe@example.com",
                        new HashSet<>());
        JobCandidate jobCandidate2 =
                new JobCandidate(
                        2,
                        "Jane Doe",
                        LocalDate.of(1995, 1, 1),
                        "0987654321",
                        "jane.doe@example.com",
                        new HashSet<>());
        List<JobCandidate> jobCandidates = new ArrayList<>();
        jobCandidates.add(jobCandidate1);
        jobCandidates.add(jobCandidate2);

        JobCandidateDto jobCandidateDto1 =
                new JobCandidateDto(
                        1,
                        "John Doe",
                        LocalDate.of(1990, 1, 1),
                        "1234567890",
                        "john.doe@example.com",
                        new ArrayList<>());
        JobCandidateDto jobCandidateDto2 =
                new JobCandidateDto(
                        2,
                        "Jane Doe",
                        LocalDate.of(1995, 1, 1),
                        "0987654321",
                        "jane.doe@example.com",
                        new ArrayList<>());
        List<JobCandidateDto> jobCandidateDtos = new ArrayList<>();
        jobCandidateDtos.add(jobCandidateDto1);
        jobCandidateDtos.add(jobCandidateDto2);

        when(jobCandidateRepository.findAll()).thenReturn(jobCandidates);
        when(jobCandidateDtoMapper.fromJobCandidateToJobCandidateDto(jobCandidate1))
                .thenReturn(jobCandidateDto1);
        when(jobCandidateDtoMapper.fromJobCandidateToJobCandidateDto(jobCandidate2))
                .thenReturn(jobCandidateDto2);

        List<JobCandidateDto> result = jobCandidateService.findAll();

        assertEquals(jobCandidateDtos.size(), result.size());
        assertEquals(jobCandidateDtos.get(0).getId(), result.get(0).getId());
        assertEquals(jobCandidateDtos.get(1).getId(), result.get(1).getId());

        verify(jobCandidateRepository, times(1)).findAll();
        verify(jobCandidateDtoMapper, times(1)).fromJobCandidateToJobCandidateDto(jobCandidate1);
        verify(jobCandidateDtoMapper, times(1)).fromJobCandidateToJobCandidateDto(jobCandidate2);
    }
    // endregion

    //region Find job candidate by id
    @Test
    @DisplayName("Should throw JobCandidateNotFoundException when the id does not exist")
    void findByIdWhenIdDoesNotExistThenThrowException() {
        when(jobCandidateRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(JobCandidateNotFoundException.class, () -> jobCandidateService.findById(1));
        verify(jobCandidateRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Should return the job candidate when the id exists")
    void findByIdWhenIdExists() {
        Integer id = 1;
        JobCandidate jobCandidate =
                new JobCandidate(
                        id,
                        "John Doe",
                        LocalDate.of(1990, 1, 1),
                        "+123456789",
                        "john.doe@example.com",
                        new HashSet<>());
        when(jobCandidateRepository.findById(id)).thenReturn(Optional.of(jobCandidate));

        JobCandidate foundJobCandidate = null;
        try {
            foundJobCandidate = jobCandidateService.findById(id);
        } catch (JobCandidateNotFoundException e) {
            fail("Exception should not be thrown");
        }

        assertNotNull(foundJobCandidate, "Job candidate should not be null");
        assertEquals(
                jobCandidate, foundJobCandidate, "Job candidate should match the expected value");
        verify(jobCandidateRepository, times(1)).findById(id);
    }
    // endregion
}