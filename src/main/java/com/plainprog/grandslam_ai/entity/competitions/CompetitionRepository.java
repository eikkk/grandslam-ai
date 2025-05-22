package com.plainprog.grandslam_ai.entity.competitions;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
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
}