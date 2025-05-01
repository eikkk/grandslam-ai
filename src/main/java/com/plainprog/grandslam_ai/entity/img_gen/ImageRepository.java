package com.plainprog.grandslam_ai.entity.img_gen;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Integer> {
    Optional<Image> findFirstByCreatorAccountId(Integer creatorAccountId);
    Optional<Image> findFirstByCreatorAccountIdNot(Integer id);
}