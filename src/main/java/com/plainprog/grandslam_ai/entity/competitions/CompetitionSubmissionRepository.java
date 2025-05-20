package com.plainprog.grandslam_ai.entity.competitions;

import com.plainprog.grandslam_ai.entity.competitions.projections.CompetitionSubmissionsCount;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompetitionSubmissionRepository extends JpaRepository<CompetitionSubmission, Long> {
    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT COUNT(s) FROM CompetitionSubmission s WHERE s.competition.id = :competitionId")
    int countByCompetitionIdWithLock(@Param("competitionId") Long competitionId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM CompetitionSubmission s WHERE s.competition.id = :competitionId AND s.accountId = :accountId AND s.image.id = :imageId")
    CompetitionSubmission findByCompetitionIdAndAccountIdAndImageIdWithLock(
            @Param("competitionId") Long competitionId,
            @Param("accountId") Long accountId,
            @Param("imageId") Long imageId);

    List<CompetitionSubmission> findAllByCompetitionId(Long competitionId);

    @Query("SELECT cs.competition.id AS competitionId, COUNT(cs) AS submissionCount " +
            "FROM CompetitionSubmission cs " +
            "WHERE cs.competition.id IN :competitionIds " +
            "GROUP BY cs.competition.id")
    List<CompetitionSubmissionsCount> findCompetitionSubmissionCountsByCompetitionIds(@Param("competitionIds") List<Long> competitionIds);

}