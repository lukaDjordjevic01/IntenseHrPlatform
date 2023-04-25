package com.example.internshipIntense.controller.candidate;

import com.example.internshipIntense.dto.candidate.JobCandidateDto;
import com.example.internshipIntense.dto.candidate.JobCandidateListDto;
import com.example.internshipIntense.exception.candidate.EmailAlreadyExistsException;
import com.example.internshipIntense.exception.candidate.JobCandidateAlreadyHasSkillException;
import com.example.internshipIntense.exception.candidate.JobCandidateHasNoSkillException;
import com.example.internshipIntense.exception.candidate.JobCandidateNotFoundException;
import com.example.internshipIntense.exception.skill.SkillNotFoundException;
import com.example.internshipIntense.helper.ErrorMessage;
import com.example.internshipIntense.service.candidate.JobCandidateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(JobCandidateController.class)
public class JobCandidateControllerTest {

    private static final String CONTROLLER_URL = "http://localhost:8080/api/jobCandidates";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobCandidateService jobCandidateService;

    @Autowired
    private JobCandidateController jobCandidateController;

    //region Search job candidates by skills
    @Test
    @DisplayName("Should throw an exception when a skill is not found")
    void searchJobCandidateBySkillsWithNonExistingSkillThenThrowException() throws Exception {
        List<Integer> skillIds = Collections.singletonList(1);
        String errorMessage = "Skill not found";

        when(jobCandidateService.searchJobCandidateBySkills(skillIds))
                .thenThrow(new SkillNotFoundException(errorMessage));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(CONTROLLER_URL
                        + "/searchBySkills")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(skillIds));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());

        verify(jobCandidateService, times(1)).searchJobCandidateBySkills(skillIds);
    }

    @Test
    @DisplayName("Should throw an exception when no job candidates have the given skills")
    void searchJobCandidateBySkillsWithNoMatchingCandidates() throws Exception {
        List<Integer> skillIds = new ArrayList<>();
        skillIds.add(1);
        skillIds.add(2);

        when(jobCandidateService.searchJobCandidateBySkills(skillIds))
                .thenThrow(new JobCandidateNotFoundException("No candidates were found!"));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(CONTROLLER_URL
                        + "/searchBySkills")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(skillIds));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());

        verify(jobCandidateService, times(1)).searchJobCandidateBySkills(skillIds);
    }

    @Test
    @DisplayName("Should return a list of job candidates with the given skills")
    void searchJobCandidateBySkillsWithGivenSkills() throws Exception {
        List<Integer> skillIds = new ArrayList<>();
        skillIds.add(1);
        skillIds.add(2);

        List<JobCandidateDto> jobCandidates = new ArrayList<>();
        jobCandidates.add(
                new JobCandidateDto(
                        1,
                        "John Smith",
                        null,
                        "+123456789",
                        "johnsmith@gmail.com",
                        new ArrayList<>()));
        jobCandidates.add(
                new JobCandidateDto(
                        2,
                        "Jane Doe",
                        null,
                        "+987654321",
                        "janedoe@gmail.com",
                        new ArrayList<>()));

        when(jobCandidateService.searchJobCandidateBySkills(skillIds)).thenReturn(jobCandidates);
        JobCandidateListDto jobCandidateListDto = new JobCandidateListDto(jobCandidates);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(CONTROLLER_URL
                        + "/searchBySkills")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(skillIds));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(jobCandidateListDto)));

        verify(jobCandidateService, times(1)).searchJobCandidateBySkills(skillIds);
    }
    //endregion

    //region Search job candidates by name
    @Test
    @DisplayName("Should throw a JobCandidateNotFoundException when an invalid name is provided")
    void searchJobCandidateByNameWithInvalidNameThrowsException() throws Exception {
        String invalidName = "Invalid Name";
        when(jobCandidateService.searchJobCandidateByName(invalidName))
                .thenThrow(new JobCandidateNotFoundException("Candidate not found"));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(CONTROLLER_URL
                        + "/searchByName")
                .queryParam("name", invalidName);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());

        verify(jobCandidateService, times(1)).searchJobCandidateByName(invalidName);
    }

    @Test
    @DisplayName("Should return a job candidate when a valid name is provided")
    void searchJobCandidateByNameWithValidName() throws Exception {
        String candidateName = "John Smith";
        JobCandidateDto jobCandidateDto =
                new JobCandidateDto(1, candidateName, null, null, null, null);
        when(jobCandidateService.searchJobCandidateByName(candidateName))
                .thenReturn(jobCandidateDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(CONTROLLER_URL
                        + "/searchByName")
                .queryParam("name", candidateName);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(jobCandidateDto)));

        verify(jobCandidateService, times(1)).searchJobCandidateByName(candidateName);
    }
    //endregion

    //region Remove job candidate
    @Test
    @DisplayName("Should remove the job candidate when the candidate id is valid")
    void removeJobCandidateWhenCandidateIdIsValid() throws Exception {
        int candidateId = 1;

        doNothing().when(jobCandidateService).removeJobCandidate(candidateId);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete(CONTROLLER_URL
                + "/" + candidateId);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());

        verify(jobCandidateService, times(1)).removeJobCandidate(candidateId);
    }

    @Test
    @DisplayName("Should throw an exception when the candidate id is not found")
    void removeJobCandidateWhenCandidateIdNotFoundThenThrowException() throws Exception {
        int candidateId = 1;
        String errorMessage = "Candidate not found";

        doThrow(new JobCandidateNotFoundException(errorMessage))
                .when(jobCandidateService).removeJobCandidate(candidateId);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete(CONTROLLER_URL
                + "/" + candidateId);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());

        verify(jobCandidateService, times(1)).removeJobCandidate(candidateId);
    }
    //endregion

    //region Remove skill from job candidate
    @Test
    @DisplayName("Should throw SkillNotFoundException when the skill does not exist")
    void removeSkillFromJobCandidateWhenSkillNotFoundThenThrowException() throws Exception {
        int candidateId = 1;
        int skillId = 2;

        when(jobCandidateService.removeSkillFromJobCandidate(candidateId, skillId))
                .thenThrow(new SkillNotFoundException("Skill not found"));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put(CONTROLLER_URL
                + "/removeSkill/" + candidateId + "/" + skillId);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());

        verify(jobCandidateService, times(1)).removeSkillFromJobCandidate(candidateId, skillId);
    }

    @Test
    @DisplayName("Should throw JobCandidateNotFoundException when the candidate does not exist")
    void removeSkillFromJobCandidateWhenCandidateNotFoundThenThrowException() throws Exception {
        int candidateId = 1;
        int skillId = 1;

        when(jobCandidateService.removeSkillFromJobCandidate(candidateId, skillId))
                .thenThrow(new JobCandidateNotFoundException("Candidate not found"));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put(CONTROLLER_URL
                + "/removeSkill/" + candidateId + "/" + skillId);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());

        verify(jobCandidateService, times(1)).removeSkillFromJobCandidate(candidateId, skillId);
    }

    @Test
    @DisplayName(
            "Should throw JobCandidateHasNoSkillException when the candidate does not have the skill")
    void removeSkillFromJobCandidateWhenCandidateHasNoSkillThenThrowException() throws Exception {
        int candidateId = 1;
        int skillId = 1;
        String errorMessage = "Candidate does not have the skill";

        when(jobCandidateService.removeSkillFromJobCandidate(candidateId, skillId))
                .thenThrow(new JobCandidateHasNoSkillException(errorMessage));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put(CONTROLLER_URL
                + "/removeSkill/" + candidateId + "/" + skillId);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());

        verify(jobCandidateService, times(1)).removeSkillFromJobCandidate(candidateId, skillId);
    }

    @Test
    @DisplayName(
            "Should remove the skill from the job candidate when both candidate and skill exist and candidate has the skill")
    void removeSkillFromJobCandidateWhenCandidateAndSkillExistAndCandidateHasSkill() throws Exception {
        Integer candidateId = 1;
        Integer skillId = 1;
        JobCandidateDto jobCandidateDto =
                new JobCandidateDto(
                        candidateId,
                        "John Smith",
                        null,
                        "+123456789",
                        "johnsmith@gmail.com",
                        new ArrayList<>());
        when(jobCandidateService.removeSkillFromJobCandidate(candidateId, skillId))
                .thenReturn(jobCandidateDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put(CONTROLLER_URL
                + "/removeSkill/" + candidateId + "/" + skillId);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(jobCandidateDto)));

        verify(jobCandidateService, times(1)).removeSkillFromJobCandidate(candidateId, skillId);
    }
    //endregion

    //region Update job candidate with skill
    @Test
    @DisplayName("Should throw JobCandidateNotFoundException when the candidate does not exist")
    void
    updateJobCandidateWithSkillWhenCandidateDoesNotExistThenThrowJobCandidateNotFoundException() throws Exception {
        int candidateId = 1;
        int skillId = 1;

        when(jobCandidateService.updateJobCandidateWithSkill(candidateId, skillId))
                .thenThrow(new JobCandidateNotFoundException("Candidate not found"));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put(CONTROLLER_URL
                + "/updateWithSkill/" + candidateId + "/" + skillId);

        System.out.println(CONTROLLER_URL
                + "/updateWithSkill/" + candidateId + "/" + skillId);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());

        verify(jobCandidateService, times(1)).updateJobCandidateWithSkill(candidateId, skillId);
    }

    @Test
    @DisplayName("Should throw SkillNotFoundException when the skill does not exist")
    void updateJobCandidateWithSkillWhenSkillDoesNotExistThenThrowSkillNotFoundException() throws Exception {
        Integer candidateId = 1;
        Integer skillId = 2;

        when(jobCandidateService.updateJobCandidateWithSkill(candidateId, skillId))
                .thenThrow(new SkillNotFoundException("Skill not found"));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put(CONTROLLER_URL
                + "/updateWithSkill/" + candidateId + "/" + skillId);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());

        verify(jobCandidateService, times(1)).updateJobCandidateWithSkill(candidateId, skillId);
    }

    @Test
    @DisplayName(
            "Should update the job candidate with a new skill when both candidate and skill exist and candidate does not have the skill")
    void updateJobCandidateWithSkillWhenCandidateAndSkillExistAndCandidateDoesNotHaveSkill() throws Exception {
        Integer candidateId = 1;
        Integer skillId = 1;
        JobCandidateDto jobCandidateDto =
                new JobCandidateDto(
                        candidateId,
                        "Jon Smith",
                        null,
                        "+38169125365",
                        "johnsmith@gmail.com",
                        new ArrayList<>());
        when(jobCandidateService.updateJobCandidateWithSkill(candidateId, skillId))
                .thenReturn(jobCandidateDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put(CONTROLLER_URL
                + "/updateWithSkill/" + candidateId + "/" + skillId);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(jobCandidateDto)));

        verify(jobCandidateService, times(1)).updateJobCandidateWithSkill(candidateId, skillId);
    }

    @Test
    @DisplayName(
            "Should throw JobCandidateAlreadyHasSkillException when the candidate already has the skill")
    void
    updateJobCandidateWithSkillWhenCandidateAlreadyHasSkillThenThrowJobCandidateAlreadyHasSkillException() throws Exception {
        Integer candidateId = 1;
        Integer skillId = 1;

        JobCandidateAlreadyHasSkillException exception =
                new JobCandidateAlreadyHasSkillException("Candidate already has the skill");

        when(jobCandidateService.updateJobCandidateWithSkill(candidateId, skillId))
                .thenThrow(exception);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put(CONTROLLER_URL
                + "/updateWithSkill/" + candidateId + "/" + skillId);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());

        verify(jobCandidateService, times(1)).updateJobCandidateWithSkill(candidateId, skillId);
    }
    //endregion

    //region Add job candidate
    @Test
    @DisplayName("Should add a new job candidate with a correct DTO")
    void addJobCandidateWithCorrectDto() throws Exception {
        JobCandidateDto jobCandidateDto =
                new JobCandidateDto(
                        1,
                        "John Smith",
                        null,
                        "+38169125365",
                        "johnsmith@gmail.com",
                        new ArrayList<>());
        when(jobCandidateService.addJobCandidate(jobCandidateDto)).thenReturn(jobCandidateDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(CONTROLLER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(jobCandidateDto));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(jobCandidateDto)));

        verify(jobCandidateService, times(1)).addJobCandidate(jobCandidateDto);
    }

    @Test
    @DisplayName("Should throw an EmailAlreadyExistsException when the email already exists")
    void addJobCandidateWhenEmailAlreadyExistsThenThrowException() throws Exception {
        JobCandidateDto jobCandidateDto =
                new JobCandidateDto(
                        1,
                        "John Smith",
                        null,
                        "+38169125365",
                        "johnsmith@gmail.com",
                        new ArrayList<>());

        when(jobCandidateService.addJobCandidate(jobCandidateDto))
                .thenThrow(EmailAlreadyExistsException.class);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(CONTROLLER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(jobCandidateDto));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());

        verify(jobCandidateService, times(1)).addJobCandidate(jobCandidateDto);
    }

    @Test
    @DisplayName("Should throw a SkillNotFoundException when the skill is not found")
    void addJobCandidateWhenSkillNotFoundThenThrowException() throws Exception {
        JobCandidateDto jobCandidateDto =
                new JobCandidateDto(
                        1,
                        "John Smith",
                        null,
                        "+38169125365",
                        "johnsmith@gmail.com",
                        new ArrayList<>());
        when(jobCandidateService.addJobCandidate(jobCandidateDto))
                .thenThrow(new SkillNotFoundException("Skill not found"));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(CONTROLLER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(jobCandidateDto));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());

        verify(jobCandidateService, times(1)).addJobCandidate(jobCandidateDto);
    }
    //endregion

    //region Get all job candidates
    @Test
    @DisplayName("Should return all job candidates")
    void getAllJobCandidates() throws Exception {
        List<JobCandidateDto> jobCandidateDtoList = new ArrayList<>();
        JobCandidateDto jobCandidateDto =
                new JobCandidateDto(
                        1,
                        "John Smith",
                        null,
                        "+38169125365",
                        "johnsmith@gmail.com",
                        Collections.emptyList());
        jobCandidateDtoList.add(jobCandidateDto);

        when(jobCandidateService.findAll()).thenReturn(jobCandidateDtoList);
        JobCandidateListDto jobCandidateListDto = new JobCandidateListDto(jobCandidateDtoList);

        mockMvc.perform(MockMvcRequestBuilders.get(CONTROLLER_URL))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(jobCandidateListDto)));

        verify(jobCandidateService, times(1)).findAll();
    }
    //endregion
}