package com.plainprog.grandslam_ai.object.response_models.generation;



import com.plainprog.grandslam_ai.entity.img_gen.ImgGenProvider;
import com.plainprog.grandslam_ai.helper.image.ModuleExamples;
import com.plainprog.grandslam_ai.object.dto.image.ImageDTO;

import java.util.List;

public class ImgGenModuleModel {
    private int id;
    private String moduleName;
    private ImgGenProvider provider;
    private List<ImageDTO> thumbnails;
    private List<ModuleExamples.ExampleEntry> examples;
    private int groupId;
    private String groupName;

    public ImgGenModuleModel() {
    }

    public ImgGenModuleModel(Integer moduleId, String moduleName, ImgGenProvider provider, List<ImageDTO> thumbnails, List<ModuleExamples.ExampleEntry> examples, int groupId, String groupName) {
        this.id = moduleId;
        this.moduleName = moduleName;
        this.thumbnails = thumbnails;
        this.examples = examples;
        this.provider = provider;
        this.groupId = groupId;
        this.groupName = groupName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public List<ImageDTO> getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(List<ImageDTO> thumbnails) {
        this.thumbnails = thumbnails;
    }

    public List<ModuleExamples.ExampleEntry> getExamples() {
        return examples;
    }

    public void setExamples(List<ModuleExamples.ExampleEntry> examples) {
        this.examples = examples;
    }

    public ImgGenProvider getProvider() {
        return provider;
    }

    public void setProvider(ImgGenProvider provider) {
        this.provider = provider;
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
}
