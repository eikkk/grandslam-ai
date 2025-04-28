package com.plainprog.grandslam_ai.object.response_models.generation;


import java.util.List;

public class ImgGenModulesResponse {
    private List<ImgGenGroupModel> groups;

    public ImgGenModulesResponse() {
    }

    public ImgGenModulesResponse(List<ImgGenGroupModel> groups) {
        this.groups = groups;
    }

    public List<ImgGenGroupModel> getGroups() {
        return groups;
    }

    public void setGroups(List<ImgGenGroupModel> groups) {
        this.groups = groups;
    }
}

