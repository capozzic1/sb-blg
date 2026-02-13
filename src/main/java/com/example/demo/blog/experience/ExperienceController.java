package com.example.demo.blog.experience;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/experience")
public class ExperienceController {

    private final ExperienceService experienceService;

    public ExperienceController(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }

    @GetMapping
    public List<ExperienceDTO> getAllExperiences() {
        return experienceService.getAllExperiences();
    }

    @GetMapping("/{id}")
    //return a responsetitvy of type experiencedto
    public ResponseEntity<ExperienceDTO> getExperienceById(Long id) {
        ExperienceDTO experience = experienceService.getExperienceById(id);
        if (experience != null) {
            return ResponseEntity.ok(experience);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ExperienceDTO> createExperience(@RequestBody ExperienceDTO experienceDto) {
        System.out.println("company=" + experienceDto.getCompany());
        ExperienceDTO created = experienceService.createExperience(experienceDto);
        return ResponseEntity.ok(created);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ExperienceDTO> updateExperience(@PathVariable Long id,@RequestBody ExperienceDTO experienceDto) {
        try {
            ExperienceDTO updated = experienceService.updateExperience(id, experienceDto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExperience(@PathVariable Long id){
        experienceService.deleteExperience(id);
        return ResponseEntity.noContent().build();
    }


}
