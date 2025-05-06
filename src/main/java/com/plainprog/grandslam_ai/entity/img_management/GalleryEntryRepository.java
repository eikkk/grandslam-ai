package com.plainprog.grandslam_ai.entity.img_management;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GalleryEntryRepository extends JpaRepository<GalleryEntry, Long> {
    List<GalleryEntry> findAllByGroupIdAndImageOwnerAccountId(Integer groupId, Long imageOwnerAccountId);

    @Query("SELECT MIN(e.position) FROM GalleryEntry e WHERE e.group.id = :groupId AND e.image.ownerAccount.id = :accountId")
    Integer findMinPositionByGroupIdAndAccountId(@Param("groupId") Integer groupId, @Param("accountId") Long accountId);
    List<GalleryEntry> findAllByImageIdIn(List<Long> imageIds);
}