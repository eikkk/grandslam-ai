package com.plainprog.grandslam_ai.entity.competitions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompetitionQueueRepository extends JpaRepository<CompetitionQueue, Long> {
    /**
     * Find all competition queue entries with given status.
     *
     * @return List of all competition queue entries with given status
     */
    List<CompetitionQueue> findAllByStatus(CompetitionQueue.CompetitionQueueStatus status);
    /**
     * Find all competition queue entries with given status and theme group.
     *
     * @param status The status to filter by
     * @param themeGroupId The ID of the theme group to filter by
     * @return List of competition queue entries with given status and theme group
     */
    @Query("SELECT cq FROM CompetitionQueue cq JOIN cq.theme t WHERE cq.status = :status AND t.themeGroup.id = :themeGroupId")
    List<CompetitionQueue> findAllByStatusAndThemeGroupId(@Param("status") CompetitionQueue.CompetitionQueueStatus status, @Param("themeGroupId") Integer themeGroupId);

    /**
     * Find the last N processed competition queue items for a specific theme group.
     * Orders by ID in descending order.
     *
     * @param limit Number of items to return
     * @param groupId The ID of the theme group to filter by
     * @return List of the last N processed competition queue items for the given theme group
     */
    @Query(value = "SELECT cq.* FROM competition_queue cq " +
            "JOIN competition_theme ct ON cq.theme_id = ct.id " +
            "WHERE cq.status = 'PROCESSED' AND ct.theme_group_id = :groupId " +
            "ORDER BY cq.id DESC LIMIT :limit",
            nativeQuery = true)
    List<CompetitionQueue> findLastProcessedItemsByThemeGroupId(@Param("limit") int limit, @Param("groupId") Integer groupId);
}