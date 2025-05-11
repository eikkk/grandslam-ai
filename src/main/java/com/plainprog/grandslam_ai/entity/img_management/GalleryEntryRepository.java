package com.plainprog.grandslam_ai.entity.img_management;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GalleryEntryRepository extends JpaRepository<GalleryEntry, Long> {
    List<GalleryEntry> findAllByGroupIdAndImageOwnerAccountId(Integer groupId, Long imageOwnerAccountId);

    @Query("SELECT MIN(e.position) FROM GalleryEntry e WHERE e.group.id = :groupId AND e.image.ownerAccount.id = :accountId")
    Integer findMinPositionByGroupIdAndAccountId(@Param("groupId") Integer groupId, @Param("accountId") Long accountId);
    Optional<GalleryEntry> findByImageId(Long imageId);
    List<GalleryEntry> findAllByImageIdIn(List<Long> imageIds);
    List<GalleryEntry> findAllByIdInAndImageOwnerAccountId(List<Long> ids, Long ownerAccountId);
    List<GalleryEntry> findAllByGroupIsNullAndHiddenAtIsNullAndImageOwnerAccountId(Long ownerAccountId);
    List<GalleryEntry> findAllByHiddenAtIsNotNullAndImageOwnerAccountId(Long ownerAccountId);
    List<GalleryEntry> findAllByImageIdInAndImageOwnerAccountId(List<Long> imageIds, Long ownerAccountId);

}