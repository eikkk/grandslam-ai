package com.plainprog.grandslam_ai.object.mappers;

import com.plainprog.grandslam_ai.entity.img_management.IncubatorEntry;
import com.plainprog.grandslam_ai.object.response_models.image_management.incubator.IncubatorResponseItem;

public class IncubatorMappers {
    // Example method to map IncubatorEntry to IncubatorResponseItem
    public static IncubatorResponseItem mapToResponseItem(IncubatorEntry entry) {
        if (entry == null) {
            return null;
        }
        return new IncubatorResponseItem(
                entry.getId(),
                entry.getImage().getId(),
                ImageMapper.mapToDTO(entry.getImage()),
                entry.isShortlisted()
        );
    }
}
