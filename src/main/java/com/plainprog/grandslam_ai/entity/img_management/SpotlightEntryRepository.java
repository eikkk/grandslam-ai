package com.plainprog.grandslam_ai.entity.img_management;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpotlightEntryRepository extends JpaRepository<SpotlightEntry, Integer> {
    boolean existsByImageId(Long imageId);
    List<SpotlightEntry> findAllByAccountId(Long accountId);
    Optional<SpotlightEntry> findByImageId(Long imageId);
    List<SpotlightEntry> findAllByImageIdInAndImageOwnerAccountId(List<Long> imageIds, Long accountId);
}