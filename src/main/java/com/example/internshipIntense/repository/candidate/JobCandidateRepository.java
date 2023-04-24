package com.example.internshipIntense.repository.candidate;

import com.example.internshipIntense.model.candidate.JobCandidate;
import com.example.internshipIntense.model.skill.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobCandidateRepository extends JpaRepository<JobCandidate, Integer> {

    Optional<JobCandidate> findByNameIgnoreCase(String name);

    @Query("SELECT jc FROM JobCandidate jc INNER JOIN jc.skills s WHERE s.id IN :skillIds " +
            "GROUP BY jc HAVING COUNT(DISTINCT s.id) = :skillCount")
    List<JobCandidate> findBySkills(List<Integer> skillIds, Integer skillCount);



    Optional<JobCandidate> findByEmail(String email);

}
