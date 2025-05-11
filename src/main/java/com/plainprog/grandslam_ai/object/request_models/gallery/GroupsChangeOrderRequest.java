package com.plainprog.grandslam_ai.object.request_models.gallery;

public class GroupsChangeOrderRequest {
    private Integer placeAfterGroupId;
    private Integer placeBeforeGroupId;

    public GroupsChangeOrderRequest() {
    }
    public GroupsChangeOrderRequest(Integer placeAfterGroupId, Integer placeBeforeGroupId) {
        this.placeAfterGroupId = placeAfterGroupId;
        this.placeBeforeGroupId = placeBeforeGroupId;
    }
    public Integer getPlaceAfterGroupId() {
        return placeAfterGroupId;
    }
    public void setPlaceAfterGroupId(Integer placeAfterGroupId) {
        this.placeAfterGroupId = placeAfterGroupId;
    }
    public Integer getPlaceBeforeGroupId() {
        return placeBeforeGroupId;
    }
    public void setPlaceBeforeGroupId(Integer placeBeforeGroupId) {
        this.placeBeforeGroupId = placeBeforeGroupId;
    }
}
