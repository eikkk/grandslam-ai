package com.plainprog.grandslam_ai.entity.img_management;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IncubatorEntryRepository extends JpaRepository<IncubatorEntry, Long> {
    IncubatorEntry findByImageId(Long imageId);
}