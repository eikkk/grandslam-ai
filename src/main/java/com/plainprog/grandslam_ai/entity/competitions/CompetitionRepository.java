package com.plainprog.grandslam_ai.entity.competitions;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompetitionRepository extends JpaRepository<Competition, Long> {
    List<Competition> findAllByStatus(Competition.CompetitionStatus status);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Competition c WHERE c.id = :id")
    Competition findByIdWithLock(@Param("id") Long id);
    
    @Query("SELECT c FROM Competition c JOIN c.theme t JOIN t.themeGroup tg WHERE c.status = :status AND tg.id = :themeGroupId")
    List<Competition> findAllByStatusAndThemeGroupId(@Param("status") Competition.CompetitionStatus status, @Param("themeGroupId") Integer themeGroupId);
}
