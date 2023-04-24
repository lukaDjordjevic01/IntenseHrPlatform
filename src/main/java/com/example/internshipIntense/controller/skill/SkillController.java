package com.example.internshipIntense.controller.skill;

import com.example.internshipIntense.dto.candidate.JobCandidateDto;
import com.example.internshipIntense.dto.skill.SkillCreationDto;
import com.example.internshipIntense.dto.skill.SkillDto;
import com.example.internshipIntense.dto.skill.SkillListDto;
import com.example.internshipIntense.exception.skill.SkillNotFoundException;
import com.example.internshipIntense.helper.ErrorMessage;
import com.example.internshipIntense.service.skill.SkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/skills")
@Tag(name = "Skills", description = "Operations about skills")
public class SkillController {

    @Autowired
    private SkillService skillService;


    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = SkillListDto.class))
    })
    @Operation(summary = "Get all skills", description = "Get all skills")
    @GetMapping
    public ResponseEntity<?> getAllSkills(){
        return new ResponseEntity<>(new SkillListDto(skillService.findAll()), HttpStatus.OK);
    }


    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = SkillDto.class))
    })
    @Operation(summary = "Add skill", description = "Add skill based on skill dto")
    @PostMapping
    public ResponseEntity<?> addSkill(@Valid @RequestBody SkillCreationDto skillCreationDto){
        return new ResponseEntity<>(skillService.addSkill(skillCreationDto), HttpStatus.OK);
    }


    @Operation(summary = "Delete skill", description = "Delete skill based on skill id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class, example = "Skill removed successfully"))}),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))})
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> removeSkill(@PathVariable Integer id){
        try {
            skillService.removeSkill(id);
            return new ResponseEntity<>("Skill successfully removed!", HttpStatus.OK);
        } catch (SkillNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
