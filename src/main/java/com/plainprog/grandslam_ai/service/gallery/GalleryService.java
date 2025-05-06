package com.plainprog.grandslam_ai.service.gallery;

import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.entity.img_gen.Image;
import com.plainprog.grandslam_ai.entity.img_management.GalleryEntry;
import com.plainprog.grandslam_ai.entity.img_management.GalleryEntryRepository;
import com.plainprog.grandslam_ai.entity.img_management.IncubatorEntry;
import com.plainprog.grandslam_ai.entity.img_management.IncubatorEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GalleryService {
    @Autowired
    private GalleryEntryRepository galleryEntryRepository;
    @Autowired
    private IncubatorEntryRepository incubatorEntryRepository;


    @Transactional
    public void promoteIncubatorImages(List<Long> imageIds, Account account) {
        //iterate through imageIds and handle each delete
        List<IncubatorEntry> incubatorEntries = incubatorEntryRepository.findAllByImageIdIn(imageIds);
        List<Image> images = incubatorEntries.stream()
                .map(IncubatorEntry::getImage)
                .toList();
        //verify image ownership
        for (Image image : images) {
            if (!image.getOwnerAccount().getId().equals(account.getId()) || !image.getCreatorAccount().getId().equals(account.getId())) {
                throw new IllegalArgumentException("Not allowed");
            }
        }
        var galleryEntries = images.stream()
                .map(image -> new GalleryEntry(image, null, false, 0, false))
                .toList();
        //save gallery entries to db
        galleryEntryRepository.saveAll(galleryEntries);
        //delete incubator entries from db
        incubatorEntryRepository.deleteAllByImageIdIn(imageIds);
    }

    public void rebalanceIndexes(Integer groupId, Long accountId) {
        // Get all entries for the given groupId and accountId
        List<GalleryEntry> entries = galleryEntryRepository.findAllByGroupIdAndImageOwnerAccountId(groupId, accountId);

        // Rebalance indexes
        int distance = 1000;
        for (int i = 0; i < entries.size(); i++) {
            entries.get(i).setPosition(i + distance);
        }

        // Save all entries back to the database
        galleryEntryRepository.saveAll(entries);
    }
}
