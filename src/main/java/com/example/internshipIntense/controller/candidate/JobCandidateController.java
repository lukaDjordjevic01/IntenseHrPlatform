package com.example.internshipIntense.controller.candidate;

import com.example.internshipIntense.dto.candidate.JobCandidateDto;
import com.example.internshipIntense.dto.candidate.JobCandidateListDto;
import com.example.internshipIntense.exception.candidate.EmailAlreadyExistsException;
import com.example.internshipIntense.exception.candidate.JobCandidateAlreadyHasSkillException;
import com.example.internshipIntense.exception.candidate.JobCandidateHasNoSkillException;
import com.example.internshipIntense.exception.candidate.JobCandidateNotFoundException;
import com.example.internshipIntense.exception.skill.SkillNotFoundException;
import com.example.internshipIntense.helper.ErrorMessage;
import com.example.internshipIntense.model.candidate.JobCandidate;
import com.example.internshipIntense.service.candidate.JobCandidateService;
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

import java.util.List;

@RestController
@RequestMapping(value = "api/jobCandidates")
@Tag(name = "Job candidates", description = "Operations about job candidates")
public class JobCandidateController {

    @Autowired
    private JobCandidateService jobCandidateService;

    @Operation(summary = "Get all candidates", description = "Get all candidates")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = JobCandidateListDto.class))
    })
    @GetMapping
    public ResponseEntity<?> getAllJobCandidates(){
        return new ResponseEntity<>(new JobCandidateListDto(jobCandidateService.findAll()), HttpStatus.OK);
    }

    @Operation(summary = "Add new candidate", description = "Add new candidate with a correct dto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = JobCandidateDto.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
            }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
            })
    })
    @PostMapping
    public ResponseEntity<?> addJobCandidate(@Valid @RequestBody JobCandidateDto jobCandidateDto){
        try {
            return new ResponseEntity<>(jobCandidateService.addJobCandidate(jobCandidateDto), HttpStatus.OK);
        } catch (EmailAlreadyExistsException e) {
            return new ResponseEntity<>(new ErrorMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (SkillNotFoundException e) {
            return new ResponseEntity<>(new ErrorMessage(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Update candidate with skill", description = "Update candidate by adding a " +
            "new skill to its skill set based on skill id and candidate id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = JobCandidateDto.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
            }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
            })
    })
    @PutMapping(value = "/updateWithSkill/{candidateId}/{skillId}")
    public ResponseEntity<?> updateJobCandidateWithSkill(@PathVariable Integer candidateId,
                                                         @PathVariable Integer skillId){
        try {
            return new ResponseEntity<>(jobCandidateService.updateJobCandidateWithSkill(candidateId, skillId),
                    HttpStatus.OK);
        } catch (JobCandidateNotFoundException | SkillNotFoundException e) {
            return new ResponseEntity<>(new ErrorMessage(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (JobCandidateAlreadyHasSkillException e) {
            return new ResponseEntity<>(new ErrorMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Remove skill from candidate", description = "Remove skill from candidate based on " +
            "skill id and candidate id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = JobCandidateDto.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
            }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
            })
    })
    @PutMapping(value = "/removeSkill/{candidateId}/{skillId}")
    public ResponseEntity<?> removeSkillFromJobCandidate(@PathVariable Integer candidateId, @PathVariable Integer skillId){
        try {
            return new ResponseEntity<>(jobCandidateService.removeSkillFromJobCandidate(candidateId, skillId),
                    HttpStatus.OK);
        } catch (JobCandidateNotFoundException | SkillNotFoundException e) {
            return new ResponseEntity<>(new ErrorMessage(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (JobCandidateHasNoSkillException e) {
            return new ResponseEntity<>(new ErrorMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Delete candidate", description = "Delete candidate based on candidate id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class, example = "Candidate removed successfully"))}),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))})
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> removeJobCandidate(@PathVariable Integer id){
        try {
            jobCandidateService.removeJobCandidate(id);
            return new ResponseEntity<>("Candidate removed successfully", HttpStatus.OK);
        } catch (JobCandidateNotFoundException e) {
            return new ResponseEntity<>(new ErrorMessage(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Search candidate by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = JobCandidateDto.class))
            }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
            })
    })
    @GetMapping(value = "/searchByName")
    public ResponseEntity<?> searchJobCandidateByName(@RequestParam String name){
        try {
            return new ResponseEntity<>(jobCandidateService.searchJobCandidateByName(name), HttpStatus.OK);
        } catch (JobCandidateNotFoundException e) {
            return new ResponseEntity<>(new ErrorMessage(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Search candidate by skills")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = JobCandidateListDto.class,
                            type = "array"))
            }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
            })
    })
    @GetMapping(value = "/searchBySkills")
    public ResponseEntity<?> searchJobCandidateBySkills(@RequestBody List<Integer> skillIds){
        try {
            return new ResponseEntity<>(new JobCandidateListDto(jobCandidateService.searchJobCandidateBySkills(skillIds)),
                    HttpStatus.OK);
        } catch (JobCandidateNotFoundException | SkillNotFoundException e) {
            return new ResponseEntity<>(new ErrorMessage(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }


}
