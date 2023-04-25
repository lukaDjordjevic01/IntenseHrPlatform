package com.example.internshipIntense.controller.candidate;

import com.example.internshipIntense.dto.candidate.JobCandidateDto;
import com.example.internshipIntense.dto.candidate.JobCandidateListDto;
import com.example.internshipIntense.service.candidate.JobCandidateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(JobCandidateController.class)
public class JobCandidateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobCandidateService jobCandidateService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JobCandidateController jobCandidateController;

    @Test
    @DisplayName("Should return all job candidates")
    void getAllJobCandidates() throws Exception {
//        JobCandidateDto jobCandidate =
//                new JobCandidateDto(
//                        1,
//                        "John Doe",
//                        LocalDate.of(1990, 1, 1),
//                        "1234567890",
//                        "john.doe@example.com",
//                        new ArrayList<>());
//        List<JobCandidateDto> jobCandidates = Collections.singletonList(jobCandidate);
//        when(jobCandidateService.findAll()).thenReturn(jobCandidates);
//
//        ResponseEntity<?> responseEntity = mockMvc.perform(MockMvcRequestBuilders.get("/jobCandidates"))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertTrue(responseEntity.getBody() instanceof JobCandidateListDto);
//        JobCandidateListDto jobCandidateListDto = (JobCandidateListDto) responseEntity.getBody();
//        assertEquals(1, jobCandidateListDto.getTotalCount());
//        assertEquals(jobCandidates.size(), jobCandidateListDto.getResults().size());
//        verify(jobCandidateService, times(1)).findAll();
    }
}