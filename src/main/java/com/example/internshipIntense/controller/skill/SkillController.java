package com.example.internshipIntense.controller.skill;

import com.example.internshipIntense.dto.skill.SkillDto;
import com.example.internshipIntense.exception.skill.SkillNotFoundException;
import com.example.internshipIntense.service.skill.SkillService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/skills")
public class SkillController {

    @Autowired
    private SkillService skillService;

    @GetMapping
    public ResponseEntity<?> getAllSkills(){
        return new ResponseEntity<>(skillService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addSkill(@Valid @RequestBody SkillDto skillDto){
        return new ResponseEntity<>(skillService.addSkill(skillDto), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> removeSkill(@PathVariable Integer id){
        try {
            skillService.removeSkill(id);
            return new ResponseEntity<>("Skill successfully removed!", HttpStatus.OK);
        } catch (SkillNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateSkill(@PathVariable Integer id, @Valid @RequestBody SkillDto skillDto){
        //implement method and service calls
        return null;
    }



}
