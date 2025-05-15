package com.plainprog.grandslam_ai.entity.competitions;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompetitionThemeRepository extends JpaRepository<CompetitionTheme, Integer> {
    /**
     * Find all non-disabled competition themes for a specific theme group.
     *
     * @param themeGroupId The ID of the theme group to search within
     * @return List of active competition themes belonging to the specified group
     */
    List<CompetitionTheme> findAllByThemeGroupIdAndDisabledFalse(Integer themeGroupId);
}