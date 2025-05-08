package com.plainprog.grandslam_ai.entity.img_management;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncubatorEntryRepository extends JpaRepository<IncubatorEntry, Long> {
    IncubatorEntry findByImageId(Long imageId);
    void deleteAllByImageIdIn(Iterable<Long> imageIds);
    List<IncubatorEntry> findAllByImageIdIn(Iterable<Long> imageIds);
    List<IncubatorEntry> findAllByImageOwnerAccountId(Long ownerAccountId);
}