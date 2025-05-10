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
        //ungrouped gallery entries can't be ordered, that's we set position to 0
        var galleryEntries = images.stream()
                .map(image -> new GalleryEntry(image, null, false, 0L, false))
                .toList();
        //save gallery entries to db
        galleryEntryRepository.saveAll(galleryEntries);
        //delete incubator entries from db
        incubatorEntryRepository.deleteAllByImageIdIn(imageIds);
    }
}
