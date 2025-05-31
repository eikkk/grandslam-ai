package com.plainprog.grandslam_ai.service.gallery;

import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.entity.img_gen.Image;
import com.plainprog.grandslam_ai.entity.img_gen.ImageRepository;
import com.plainprog.grandslam_ai.entity.img_management.*;
import com.plainprog.grandslam_ai.helper.sorting.SortingService;
import com.plainprog.grandslam_ai.object.mappers.GalleryMappers;
import com.plainprog.grandslam_ai.object.request_models.gallery.CreateGalleryGroupRequest;
import com.plainprog.grandslam_ai.object.request_models.gallery.GroupsChangeOrderRequest;
import com.plainprog.grandslam_ai.object.request_models.gallery.UpdateGalleryRequest;
import com.plainprog.grandslam_ai.object.response_models.image_management.gallery.GalleryEntryUI;
import com.plainprog.grandslam_ai.object.response_models.image_management.gallery.GalleryGroupUI;
import com.plainprog.grandslam_ai.object.response_models.image_management.gallery.GalleryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GalleryService {
    public final static int MAX_SPOTLIGHTED_IMAGES = 12;
    @Autowired
    private GalleryEntryRepository galleryEntryRepository;
    @Autowired
    private IncubatorEntryRepository incubatorEntryRepository;
    @Autowired
    private GalleryGroupRepository galleryGroupRepository;
    @Autowired
    private SortingService sortingService;
    @Autowired
    private SpotlightEntryRepository spotlightEntryRepository;
    @Autowired
    private ImageRepository imageRepository;



    public GalleryEntry getGalleryEntryByImageId(Long imageId) {
        return galleryEntryRepository.findByImageId(imageId)
                .orElseThrow(() -> new IllegalArgumentException("Gallery entry not found for image ID: " + imageId));
    }

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
                .map(image -> new GalleryEntry(image, null, null, 0L, false))
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

    @Transactional
    public void batchHideItems(List<Long> imageIds, Account account) {
        // Find entries by IDs and owner in a single query
        List<GalleryEntry> galleryEntries = galleryEntryRepository.findAllByImageIdInAndImageOwnerAccountId(imageIds, account.getId());

        // If we didn't find all requested entries, some don't belong to this user
        if (galleryEntries.size() < imageIds.size()) {
            throw new IllegalArgumentException("Not allowed to hide one or more of these images");
        }

        // Find spotlight entries by image ids
        List<SpotlightEntry> spotlightEntries = spotlightEntryRepository.findAllByImageIdIn(imageIds);

        // If image is spotlighted, remove it from spotlight
        if (!spotlightEntries.isEmpty()) {
            for (SpotlightEntry spotlightEntry : spotlightEntries) {
                spotlightEntryRepository.delete(spotlightEntry);
            }
        }

        // Hide the entries
        galleryEntries.forEach(entry -> entry.setHiddenAt(Instant.now()));

        // Save the updated entries
        galleryEntryRepository.saveAll(galleryEntries);
    }

    @Transactional
    public void batchMoveItems(List<Long> imageIds, Integer targetGroupId, Account account) {
        // Find entries by IDs and owner in a single query
        List<GalleryEntry> galleryEntries = galleryEntryRepository.findAllByImageIdInAndImageOwnerAccountId(
                imageIds, account.getId());

        // If we didn't find all requested entries, some don't belong to this user
        if (galleryEntries.size() < imageIds.size()) {
            throw new IllegalArgumentException("Not allowed to move one or more of these images");
        }

        // If targetGroupId is not null, verify the group exists and belongs to the account
        GalleryGroup targetGroup;
        if (targetGroupId != null) {
            targetGroup = galleryGroupRepository.findById(targetGroupId)
                    .orElseThrow(() -> new IllegalArgumentException("Target gallery group not found"));

            // Verify ownership of the target group
            if (!targetGroup.getAccount().getId().equals(account.getId())) {
                throw new IllegalArgumentException("Not allowed to move items to this gallery group");
            }
        } else {
            targetGroup = null; // No group specified, set to null
        }

        // Update each entry's group
        galleryEntries.forEach(entry -> entry.setGroup(targetGroup));

        // Save the updated entries
        galleryEntryRepository.saveAll(galleryEntries);
    }

    @Transactional
    public void deleteGroup(Integer id, Account account) {
        // Find the gallery group by ID
        GalleryGroup group = galleryGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Gallery group not found"));

        // Verify ownership
        if (!group.getAccount().getId().equals(account.getId())) {
            throw new IllegalArgumentException("Not allowed to delete this gallery group");
        }

        // Check if the group is empty
        if (!group.getEntries().isEmpty()) {
            throw new IllegalArgumentException("Cannot delete a gallery group that contains images");
        }

        // Delete the group
        galleryGroupRepository.delete(group);
    }

    @Transactional
    public void batchShortlistItems(List<Long> imageIds, boolean shortlisted, Account account) {
        // Find entries by IDs and owner in a single query
        List<GalleryEntry> galleryEntries = galleryEntryRepository.findAllByImageIdInAndImageOwnerAccountId(
                imageIds, account.getId());

        //filter to take only hidden entries
        List<GalleryEntry> suitableEntries = galleryEntries.stream()
                .filter(entry -> entry.getHiddenAt() != null)
                .toList();

        // Update shortlist status for all entries
        suitableEntries.forEach(entry -> entry.setShortlisted(shortlisted));

        // Save the updated entries
        galleryEntryRepository.saveAll(suitableEntries);
    }

    @Transactional
    public void reorderGroupItems(Integer groupId, List<Long> entryIds, Account account) {
        // Find the gallery group by ID
        GalleryGroup group = galleryGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Gallery group not found"));

        // Verify ownership
        if (!group.getAccount().getId().equals(account.getId())) {
            throw new IllegalArgumentException("Not allowed to reorder items in this gallery group");
        }

        // Find entries by IDs and owner
        List<GalleryEntry> galleryEntries = galleryEntryRepository.findAllByIdInAndImageOwnerAccountId(
                entryIds, account.getId());

        // If we didn't find all requested entries, some don't belong to this user
        if (galleryEntries.size() < entryIds.size()) {
            throw new IllegalArgumentException("Not allowed to reorder one or more of these images");
        }

        // Verify all entries belong to the specified group
        for (GalleryEntry entry : galleryEntries) {
            if (entry.getGroup() == null || !entry.getGroup().getId().equals(groupId)) {
                throw new IllegalArgumentException("One or more items do not belong to the specified group");
            }
        }

        // Update positions based on new order
        for (int i = 0; i < entryIds.size(); i++) {
            Long itemId = entryIds.get(i);
            // Find entry with this ID
            GalleryEntry entry = galleryEntries.stream()
                    .filter(e -> e.getId().equals(itemId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Item not found"));

            // Update position
            entry.setPosition(SortingService.GAP * (i + 1));
        }

        // Save updated entries
        galleryEntryRepository.saveAll(galleryEntries);
    }

    /**
     * Retrieve the gallery for an account.
     *
     * @param account The account to retrieve the gallery for
     * @return GalleryResponse containing organized gallery data
     */
    @Transactional(readOnly = true)
    public GalleryResponse getGallery(Account account) {
        // Fetch all groups for this account that are not hidden
        List<GalleryGroup> groups = galleryGroupRepository.findAllByAccountId(account.getId());
        // Get all spotlighted images for this account
        List<SpotlightEntry> spotlightEntries = spotlightEntryRepository.findAllByAccountId(account.getId());
        List<Long> spotlightedImageIds = spotlightEntries.stream()
                .map(SpotlightEntry::getImageId)
                .toList();

        // Map groups to response models
        List<GalleryGroupUI> responseGroups = groups.stream()
            .map(group -> GalleryMappers.mapToGalleryGroupUI(group, spotlightedImageIds))
            .sorted(Comparator.comparingLong(GalleryGroupUI::getPosition))
            .collect(Collectors.toList());

        // Get ungrouped items (not in any group and not hidden)
        List<GalleryEntry> ungroupedEntriesDB = galleryEntryRepository.findAllByGroupIsNullAndHiddenAtIsNullAndImageOwnerAccountId(
            account.getId());
        List<GalleryEntryUI> ungroupedEntries = ungroupedEntriesDB.stream()
            .sorted(Comparator.comparing(GalleryEntry::getCreatedAt).reversed())
            .map(entry -> {
                boolean spotlighted = spotlightedImageIds.contains(entry.getImage().getId());
                return GalleryMappers.mapToGalleryEntryUI(entry, spotlighted);
            })
            .collect(Collectors.toList());

        // Get hidden items
        List<GalleryEntry> hiddenEntriesDB = galleryEntryRepository.findAllByHiddenAtIsNotNullAndImageOwnerAccountId(
            account.getId());
        List<GalleryEntryUI> hiddenEntries = hiddenEntriesDB.stream()
            .sorted(Comparator.comparing(GalleryEntry::getHiddenAt).reversed())
            .map(entry -> GalleryMappers.mapToGalleryEntryUI(entry, false))
            .collect(Collectors.toList());

        // Create the response with mapped entries
        return new GalleryResponse(responseGroups, ungroupedEntries, hiddenEntries);
    }

    @Transactional
    public void spotlightImage(Long imageId, Account account) {
        // Find the gallery entry by ID
        GalleryEntry entry = galleryEntryRepository.findByImageId(imageId)
                .orElseThrow(() -> new IllegalArgumentException("Gallery entry not found"));

        if(entry.getHiddenAt() != null) {
            throw new IllegalArgumentException("Cannot spotlight a hidden image");
        }

        // Verify ownership
        if (!entry.getImage().getOwnerAccount().getId().equals(account.getId())) {
            throw new IllegalArgumentException("Ownership verification failed");
        }
        // Get all spotlighted images of account
        List<SpotlightEntry> spotlightEntries = spotlightEntryRepository.findAllByAccountId(account.getId());

        // Check if the image is already spotlighted
        if (spotlightEntries.stream().anyMatch(spotlightEntry -> spotlightEntry.getImageId().equals(imageId))) {
            throw new IllegalArgumentException("Image is already spotlighted");
        }

        // Check if the maximum number of spotlighted images is reached
        if (spotlightEntries.size() >= MAX_SPOTLIGHTED_IMAGES) {
            throw new IllegalArgumentException("Maximum number of spotlighted images reached");
        }

        // Get the position of the new spotlight entry (place first)
        Long leastPosition = spotlightEntries.stream()
                .map(SpotlightEntry::getPosition)
                .min(Long::compareTo).orElse(1L);
        Long position = leastPosition - 1;

        // Create a new spotlight entry
        SpotlightEntry spotlightEntry = new SpotlightEntry(entry.getImage().getId(), position, Instant.now());
        spotlightEntryRepository.save(spotlightEntry);
    }

    @Transactional
    public void removeSpotlightImage(Long imageId, Account account) {
        // Find the spotlight entry by image ID
        SpotlightEntry spotlightEntry = spotlightEntryRepository.findByImageId(imageId)
                .orElseThrow(() -> new IllegalArgumentException("Spotlight entry not found"));

        // Find the image by ID
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("Image not found"));


        // Verify ownership
        if (!image.getOwnerAccount().getId().equals(account.getId())) {
            throw new IllegalArgumentException("Ownership verification failed");
        }

        // Delete the spotlight entry
        spotlightEntryRepository.delete(spotlightEntry);
    }
}
