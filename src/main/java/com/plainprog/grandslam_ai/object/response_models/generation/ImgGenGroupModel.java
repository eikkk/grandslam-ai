package com.plainprog.grandslam_ai.object.response_models.generation;


import java.util.List;

public class ImgGenGroupModel {
    private int groupId;
    private String groupName;
    private List<ImgGenModuleModel> modules;

    public ImgGenGroupModel() {
    }

    public ImgGenGroupModel(int groupId, String groupName, List<ImgGenModuleModel> modules) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.modules = modules;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<ImgGenModuleModel> getModules() {
        return modules;
    }

    public void setModules(List<ImgGenModuleModel> modules) {
        this.modules = modules;
    }
}
