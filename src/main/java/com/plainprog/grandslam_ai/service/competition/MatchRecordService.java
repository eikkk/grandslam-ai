package com.plainprog.grandslam_ai.service.competition;

import com.plainprog.grandslam_ai.entity.competitions.CompetitionMatch;
import com.plainprog.grandslam_ai.entity.competitions.ImageMatchRecord;
import com.plainprog.grandslam_ai.entity.competitions.ImageMatchRecordRepository;
import com.plainprog.grandslam_ai.entity.img_gen.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class MatchRecordService {

    private final ImageMatchRecordRepository recordRepository;

    @Autowired
    public MatchRecordService(ImageMatchRecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }
    
    /**
     * Create and save ImageMatchRecord for a specific image in a match
     */
    @Transactional
    public ImageMatchRecord createMatchRecord(
            CompetitionMatch match, 
            Image image, 
            Long opponentImageId, 
            int startingElo, 
            int endingElo, 
            Integer votesCount, 
            Integer opponentVotesCount) {
        
        ImageMatchRecord record = new ImageMatchRecord(
                match, 
                image, 
                image.getOwnerAccount(), 
                opponentImageId,
                match.getRound(),
                match.getRoundInverse(),
                startingElo, 
                endingElo, 
                votesCount, 
                opponentVotesCount
        );
        
        record.setInsertedAt(Instant.now());
        return recordRepository.save(record);
    }
    
    /**
     * Create and save ImageMatchRecords for both images in a match
     */
    @Transactional
    public void createMatchRecordsForBothImages(
            CompetitionMatch match,
            Image image1, 
            Image image2,
            int votes1,
            int votes2,
            int image1StartingElo, 
            int image1EndingElo,
            int image2StartingElo, 
            int image2EndingElo) {

        // Create record from image1's perspective
        createMatchRecord(
                match, 
                image1, 
                image2.getId(), 
                image1StartingElo, 
                image1EndingElo, 
                votes1, 
                votes2
        );
        
        // Create record from image2's perspective
        createMatchRecord(
                match, 
                image2, 
                image1.getId(), 
                image2StartingElo, 
                image2EndingElo, 
                votes2, 
                votes1
        );
    }
}
