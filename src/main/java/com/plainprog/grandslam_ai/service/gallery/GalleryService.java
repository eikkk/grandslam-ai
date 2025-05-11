package com.plainprog.grandslam_ai.service.gallery;

import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.entity.img_gen.Image;
import com.plainprog.grandslam_ai.entity.img_management.*;
import com.plainprog.grandslam_ai.helper.sorting.SortingService;
import com.plainprog.grandslam_ai.object.request_models.gallery.CreateGalleryGroupRequest;
import com.plainprog.grandslam_ai.object.request_models.gallery.GroupsChangeOrderRequest;
import com.plainprog.grandslam_ai.object.request_models.gallery.UpdateGalleryRequest;
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
    @Autowired
    private GalleryGroupRepository galleryGroupRepository;
    @Autowired
    private SortingService sortingService;


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

    @Transactional
    public GalleryGroup createGroup(CreateGalleryGroupRequest request, Account account) {
        //fetch group with the smallest position
        GalleryGroup leastPositionGroup = galleryGroupRepository.findFirstByOrderByPositionAsc();
        long position = leastPositionGroup != null ? leastPositionGroup.getPosition() - SortingService.GAP : SortingService.GAP;

        GalleryGroup newGroup = new GalleryGroup(request.getName(), position, account);
        return galleryGroupRepository.save(newGroup);
    }

    @Transactional
    public void updateGroup(Integer id, UpdateGalleryRequest request, Account account) {
        // Find the gallery group by ID
        GalleryGroup group = galleryGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Gallery group not found"));

        // Verify ownership
        if (!group.getAccount().getId().equals(account.getId())) {
            throw new IllegalArgumentException("Not allowed to update this gallery group");
        }

        // Update the name
        if (request.getNewName() != null)
            group.setName(request.getNewName());
        else throw new IllegalArgumentException("Null name not allowed");

        // Save the updated group
        galleryGroupRepository.save(group);
    }

    @Transactional
    public void changeGroupOrder(Integer id, GroupsChangeOrderRequest request, Account account) {
        // Find the gallery group, previous and next groups by ID
        GalleryGroup group = galleryGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Gallery group not found"));
        GalleryGroup prev = null;
        GalleryGroup next = null;
        if (request.getPlaceAfterGroupId() != null) {
            prev = galleryGroupRepository.findById(request.getPlaceAfterGroupId())
                    .orElseThrow(() -> new IllegalArgumentException("Previous group not found"));
        }
        if (request.getPlaceBeforeGroupId() != null) {
            next = galleryGroupRepository.findById(request.getPlaceBeforeGroupId())
                    .orElseThrow(() -> new IllegalArgumentException("Next group not found"));
        }

        // Verify ownership
        if (!group.getAccount().getId().equals(account.getId())) {
            throw new IllegalArgumentException("Not allowed to update this gallery group");
        }

        // Attempt to insert the group in proper place
        boolean success = sortingService.reorder(group, prev, next);
        if (success){
            // Save the updated group
            galleryGroupRepository.save(group);
        } else {
            // If reordering was not successful, we need to re-balance the positions
            List<GalleryGroup> allGroups = galleryGroupRepository.findAllByAccountId(account.getId());
            sortingService.rebalance(allGroups);
            // Save all groups to update their positions
            galleryGroupRepository.saveAll(allGroups);
        }
    }
}
