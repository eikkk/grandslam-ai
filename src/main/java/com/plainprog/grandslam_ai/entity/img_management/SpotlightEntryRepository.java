package com.plainprog.grandslam_ai.entity.img_management;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpotlightEntryRepository extends JpaRepository<SpotlightEntry, Integer> {
    boolean existsByImageId(Long imageId);
    Optional<SpotlightEntry> findByImageId(Long imageId);
    List<SpotlightEntry> findAllByImageIdIn(List<Long> imageIds);
    @Query("SELECT se FROM SpotlightEntry se JOIN Image img ON se.imageId = img.id WHERE img.ownerAccount.id = :accountId")
    List<SpotlightEntry> findAllByAccountId(@Param("accountId") Long accountId);

}