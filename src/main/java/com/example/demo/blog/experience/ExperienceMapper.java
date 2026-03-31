package com.example.demo.blog.experience;

public class ExperienceMapper {

    public static ExperienceDTO toDto(Experience experience) {
        return new ExperienceDTO(
                experience.getId(),
                experience.getCompany(),
                experience.getPosition(),
                experience.getStartYear(),
                experience.getEndYear(),
                experience.getCreatedAt()
        );
    }

    public static Experience toEntity(ExperienceDTO experienceDto) {
        Experience experience = new Experience();
        experience.setCompany(experienceDto.getCompany());
        experience.setPosition(experienceDto.getPosition());
        experience.setStartYear(experienceDto.getStartYear());
        experience.setEndYear(experienceDto.getEndYear());

        return experience;
    }
}