package com.example.demo.blog.experience;

import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ExperienceService {

    private final ExperienceRepository experienceRepository;

    public ExperienceService(ExperienceRepository experienceRepository) {
        this.experienceRepository = experienceRepository;
    }

    public ExperienceDTO createExperience(ExperienceDTO experienceDto) {
        Experience experience = ExperienceMapper.toEntity(experienceDto);
        Experience savedExperience = experienceRepository.save(experience);
        return ExperienceMapper.toDto(savedExperience);
    }

    public List<ExperienceDTO> getAllExperiences() {
        return experienceRepository.findAll().stream()
                .map(ExperienceMapper::toDto)
                .toList();
    }

    public ExperienceDTO getExperienceById(Long id) {
        return experienceRepository.findById(id)
                .map(ExperienceMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Experience not found"));
    }

    public ExperienceDTO updateExperience(Long id, ExperienceDTO experienceDto) {
        return experienceRepository.findById(id)
                .map(existing -> {
                    existing.setCompany(experienceDto.getCompany());
                    existing.setPosition(experienceDto.getPosition());
                    existing.setYears(experienceDto.getYears());
                    Experience updated = experienceRepository.save(existing);
                    return ExperienceMapper.toDto(updated);
                })
                .orElseThrow(() -> new RuntimeException("Experience not found"));
    }

    public void deleteExperience(Long id) {
        experienceRepository.deleteById(id);
    }
}
