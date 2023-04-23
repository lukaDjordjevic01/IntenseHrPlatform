package com.example.internshipIntense.controller.candidate;

import com.example.internshipIntense.dto.candidate.JobCandidateDto;
import com.example.internshipIntense.exception.candidate.EmailAlreadyExistsException;
import com.example.internshipIntense.exception.candidate.JobCandidateAlreadyHasSkillException;
import com.example.internshipIntense.exception.candidate.JobCandidateHasNoSkillException;
import com.example.internshipIntense.exception.candidate.JobCandidateNotFoundException;
import com.example.internshipIntense.exception.skill.SkillNotFoundException;
import com.example.internshipIntense.service.candidate.JobCandidateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/jobCandidates")
public class JobCandidateController {

    @Autowired
    private JobCandidateService jobCandidateService;

    @GetMapping
    public ResponseEntity<?> getAllJobCandidates(){
        return new ResponseEntity<>(jobCandidateService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addJobCandidate(@Valid @RequestBody JobCandidateDto jobCandidateDto){
        try {
            return new ResponseEntity<>(jobCandidateService.addJobCandidate(jobCandidateDto), HttpStatus.OK);
        } catch (EmailAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "updateWithSkill/{candidateId}/{skillId}")
    public ResponseEntity<?> updateJobCandidateWithSkill(@PathVariable Integer candidateId,
                                                         @PathVariable Integer skillId){
        try {
            return new ResponseEntity<>(jobCandidateService.updateJobCandidateWithSkill(candidateId, skillId),
                    HttpStatus.OK);
        } catch (JobCandidateNotFoundException | SkillNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (JobCandidateAlreadyHasSkillException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/removeSkill/{candidateId}/{skillId}")
    public ResponseEntity<?> removeSkillFromJobCandidate(@PathVariable Integer candidateId, @PathVariable Integer skillId){
        try {
            return new ResponseEntity<>(jobCandidateService.removeSkillFromJobCandidate(candidateId, skillId),
                    HttpStatus.OK);
        } catch (JobCandidateNotFoundException | SkillNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (JobCandidateHasNoSkillException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> removeJobCandidate(@PathVariable Integer id){
        try {
            jobCandidateService.removeJobCandidate(id);
            return new ResponseEntity<>("Candidate removed successfully", HttpStatus.OK);
        } catch (JobCandidateNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/searchByName")
    public ResponseEntity<?> searchJobCandidateByName(@RequestParam String name){
        //maybe change for it to get a list of candidates that contain a name
        try {
            return new ResponseEntity<>(jobCandidateService.searchJobCandidateByName(name), HttpStatus.OK);
        } catch (JobCandidateNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/searchBySkills")
    public ResponseEntity<?> searchJobCandidateBySkills(@RequestBody List<Integer> skillIds){
        try {
            return new ResponseEntity<>(jobCandidateService.searchJobCandidateBySkills(skillIds), HttpStatus.OK);
        } catch (JobCandidateNotFoundException | SkillNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


}
