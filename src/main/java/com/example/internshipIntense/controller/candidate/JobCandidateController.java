package com.example.internshipIntense.controller.candidate;

import com.example.internshipIntense.dto.candidate.JobCandidateDto;
import com.example.internshipIntense.dto.skill.SkillDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/jobCandidates")
public class JobCandidateController {
    //Autowire a service here

    @GetMapping
    public ResponseEntity<?> getAllJobCandidates(){
        return null;
    }

    @PostMapping
    public ResponseEntity<?> addJobCandidate(@Valid @RequestBody JobCandidateDto jobCandidateDto){
        //implement method and service calls
        return null;
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateJobCandidate(@Valid @RequestBody JobCandidateDto jobCandidateDto,
                                                @PathVariable Integer id){
        //implement method and service calls
        return null;
    }

    @PutMapping(value = "/{id}/removeSkill/{skillId}")
    public ResponseEntity<?> removeSkillFromJobCandidate(@PathVariable Integer id, @PathVariable Integer skillId){
        //implement method and service calls
        return null;
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> removeJobCandidate(@PathVariable Integer id){
        //implement method and service calls
        return null;
    }

    @GetMapping(value = "/searchByName")
    public ResponseEntity<?> searchJobCandidateByName(@RequestParam String name){
        //implement method and service calls
        return null;
    }

    @GetMapping(value = "/searchBySkills")
    public ResponseEntity<?> searchJobCandidateBySkills(@Valid @RequestBody List<SkillDto> skillDtos){
        //implement method and service calls
        return null;
    }

}
