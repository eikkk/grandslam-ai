package com.plainprog.grandslam_ai.entity.competitions;

import com.plainprog.grandslam_ai.object.dto.competition.SubmissionVoteCountDTO;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchVoteRepository extends JpaRepository<MatchVote, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT COUNT(v) FROM MatchVote v WHERE v.match.id = :matchId AND v.submission.id = :submissionId")
    long countByMatchIdAndSubmissionIdWithLock(@Param("matchId") Long matchId, @Param("submissionId") Long submissionId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END FROM MatchVote v WHERE v.account.id = :accountId AND v.match.id = :matchId")
    boolean existsByAccountIdAndMatchId(@Param("accountId") Long accountId, @Param("matchId") Long matchId);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT new com.plainprog.grandslam_ai.object.dto.competition.SubmissionVoteCountDTO(v.submission.id, COUNT(v)) " +
           "FROM MatchVote v WHERE v.match.id = :matchId " +
           "GROUP BY v.submission.id")
    List<SubmissionVoteCountDTO> countVotesBySubmissionForMatchWithLock(@Param("matchId") Long matchId);
}
