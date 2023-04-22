package com.example.internshipIntense.controller.skill;

import com.example.internshipIntense.dto.skill.SkillDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/skills")
public class SkillController {

    //Autowire a service here

    @GetMapping
    public ResponseEntity<?> getAllSkills(){
        //implement method and service calls
        return null;
    }

    @PostMapping
    public ResponseEntity<?> addSkill(@Valid @RequestBody SkillDto skillDto){
        //implement method and service calls
        return null;
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> removeSkill(@PathVariable Integer id){
        //implement method and service calls
        return null;
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateSkill(@PathVariable Integer id, @Valid @RequestBody SkillDto skillDto){
        //implement method and service calls
        return null;
    }

}
