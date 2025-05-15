package com.plainprog.grandslam_ai.entity.competitions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompetitionThemeGroupRepository extends JpaRepository<CompetitionThemeGroup, Integer> {
    /**
     * Find all theme groups that have competitions with the specified status.
     * This query traverses from theme groups through themes to competitions.
     *
     * @param status The competition status to filter by
     * @return List of theme groups that have competitions with the specified status
     */
    @Query("SELECT DISTINCT tg FROM CompetitionThemeGroup tg " +
            "JOIN CompetitionTheme t ON t.themeGroup.id = tg.id " +
            "JOIN Competition c ON c.theme.id = t.id " +
            "WHERE c.status = :status")
    List<CompetitionThemeGroup> findAllByCompetitionsStatus(@Param("status") Competition.CompetitionStatus status);

    /**
     * Find all theme groups that are not disabled (active theme groups).
     *
     * @return List of active theme groups
     */
    List<CompetitionThemeGroup> findAllByDisabledFalse();
}