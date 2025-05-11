package com.plainprog.grandslam_ai.object.mappers;

import com.plainprog.grandslam_ai.entity.img_management.GalleryEntry;
import com.plainprog.grandslam_ai.entity.img_management.GalleryGroup;
import com.plainprog.grandslam_ai.object.response_models.image_management.gallery.GalleryEntryUI;
import com.plainprog.grandslam_ai.object.response_models.image_management.gallery.GalleryGroupUI;

public class GalleryMappers {
    public static GalleryEntryUI mapToGalleryEntryUI(GalleryEntry entity){
        if (entity == null) {
            return null;
        }
        return new GalleryEntryUI(
                entity.getId(),
                entity.getPosition(),
                entity.getHiddenAt(),
                entity.isShortlisted(),
                entity.getImage().getId(),
                ImageMapper.mapToDTO(entity.getImage())
        );
    }

    public static GalleryGroupUI mapToGalleryGroupUI(GalleryGroup entity){
        if (entity == null) {
            return null;
        }
        return new GalleryGroupUI(
                entity.getId(),
                entity.getName(),
                entity.getPosition(),
                entity.getEntries().stream()
                        .filter(entry -> entry.getHiddenAt() == null)
                        .map(GalleryMappers::mapToGalleryEntryUI)
                        .toList()
        );
    }
}
