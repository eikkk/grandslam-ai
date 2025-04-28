package com.plainprog.grandslam_ai.object.response_models.generation;

import java.util.List;

public class GetImgAI_ModelsResponse {
    private List<GetImgAI_Model> models;

    public GetImgAI_ModelsResponse(List<GetImgAI_Model> models) {
        this.models = models;
    }

    public List<GetImgAI_Model> getModels() {
        return models;
    }

    public void setModels(List<GetImgAI_Model> models) {
        this.models = models;
    }
}
