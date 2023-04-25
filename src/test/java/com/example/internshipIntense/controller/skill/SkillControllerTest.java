package com.example.internshipIntense.controller.skill;

import com.example.internshipIntense.dto.skill.SkillCreationDto;
import com.example.internshipIntense.dto.skill.SkillDto;
import com.example.internshipIntense.dto.skill.SkillListDto;
import com.example.internshipIntense.exception.skill.SkillNotFoundException;
import com.example.internshipIntense.service.skill.SkillService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SkillController.class)
@Tag(name = "Skills", description = "Operations about skills")
public class SkillControllerTest {

    private static final String CONTROLLER_URL = "http://localhost:8080/api/skills";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SkillService skillService;

    @Autowired
    private SkillController skillController;

    //region Remove skill
    @Test
    @DisplayName("Should throw SkillNotFoundException when the skill id is not found")
    void removeSkillWhenSkillIdNotFoundThenThrowSkillNotFoundException() throws Exception {
        int skillId = 1;

        doThrow(new SkillNotFoundException("Skill not found")).when(skillService).removeSkill(skillId);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete(CONTROLLER_URL + "/"
                + skillId);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());

        verify(skillService, times(1)).removeSkill(skillId);
    }

    @Test
    @DisplayName("Should remove the skill when the skill id is valid")
    void removeSkillWhenSkillIdIsValid() throws Exception {
        int skillId = 1;
        doNothing().when(skillService).removeSkill(skillId);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete(CONTROLLER_URL + "/"
                + skillId);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());

        verify(skillService, times(1)).removeSkill(skillId);

    }
    //endregion

    //region Add skill
    @Test
    @DisplayName("Should return a bad request when the skill creation DTO is invalid")
    void addSkillWithInvalidSkillCreationDtoThenBadRequest() throws Exception {
        SkillCreationDto invalidSkillCreationDto = new SkillCreationDto("123");

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(CONTROLLER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(invalidSkillCreationDto));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should add a new skill successfully")
    void addSkillSuccessfully() throws Exception {
        SkillCreationDto skillCreationDto = new SkillCreationDto("Java programming");
        SkillDto skillDto = new SkillDto(1, "Java programming");
        when(skillService.addSkill(skillCreationDto)).thenReturn(skillDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(CONTROLLER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(skillCreationDto));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());

        verify(skillService, times(1)).addSkill(skillCreationDto);
    }
    //endregion

    //region Get all skills
    @Test
    @DisplayName("Should return all skills")
    void getAllSkills() throws Exception {
        List<SkillDto> skillDtoList = new ArrayList<>();
        skillDtoList.add(new SkillDto(1, "Java programming"));
        skillDtoList.add(new SkillDto(2, "Python programming"));

        when(skillService.findAll()).thenReturn(skillDtoList);
        SkillListDto skillListDto = new SkillListDto(skillDtoList);

        mockMvc.perform(MockMvcRequestBuilders.get(CONTROLLER_URL))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(skillListDto)));


        verify(skillService, times(1)).findAll();
    }
    //endregion
}