package com.plainprog.grandslam_ai.entity.img_management;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GalleryGroupRepository extends JpaRepository<GalleryGroup, Integer> {
    GalleryGroup findFirstByOrderByPositionAsc();
    List<GalleryGroup> findAllByAccountId(Long accountId);
}