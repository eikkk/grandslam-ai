package com.plainprog.grandslam_ai.service.incubator;

import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.entity.img_gen.Image;
import com.plainprog.grandslam_ai.entity.img_gen.ImageRepository;
import com.plainprog.grandslam_ai.entity.img_management.IncubatorEntry;
import com.plainprog.grandslam_ai.entity.img_management.IncubatorEntryRepository;
import com.plainprog.grandslam_ai.service.gcp.GCPStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Service
public class IncubatorService {
    @Autowired
    private IncubatorEntryRepository incubatorEntryRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private GCPStorageService gcpStorageService;

    @Transactional
    public void deleteIncubatorImages(List<Long> imageIds, Account account) {
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
        //aggregate all urls: fullsize, thumbnail, compressed
        List<String> allUrls = images.stream()
                .flatMap(image -> Stream.of(image.getThumbnail(), image.getFullsize(), image.getCompressed()))
                .toList();
        //delete incubator entries from db
        incubatorEntryRepository.deleteAllByImageIdIn(imageIds);
        //delete images from db
        imageRepository.deleteAllById(imageIds);
        //delete images from gcp
        gcpStorageService.deleteImages(allUrls);
    }
}
