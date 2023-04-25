package com.example.internshipIntense.repository.skill;

import com.example.internshipIntense.model.skill.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Integer> {

    Optional<Skill> findByNameIgnoreCase(String name);
}
