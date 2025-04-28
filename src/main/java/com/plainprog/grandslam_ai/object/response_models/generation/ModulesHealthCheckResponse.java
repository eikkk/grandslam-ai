package com.plainprog.grandslam_ai.object.response_models.generation;

import com.plainprog.grandslam_ai.entity.img_gen.ImgGenModule;

import java.util.List;

public class ModulesHealthCheckResponse {
    private List<ImgGenModule> deactivatedModules;
    private String message;

    public ModulesHealthCheckResponse() {
    }

    public ModulesHealthCheckResponse(List<ImgGenModule> deactivatedModules, String message) {
        this.deactivatedModules = deactivatedModules;
        this.message = message;
    }

    public List<ImgGenModule> getDeactivatedModules() {
        return deactivatedModules;
    }

    public void setDeactivatedModules(List<ImgGenModule> deactivatedModules) {
        this.deactivatedModules = deactivatedModules;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
