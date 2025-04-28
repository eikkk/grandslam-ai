package com.plainprog.grandslam_ai.object.dto.image;

public class GetImgAI_HealthCheckRequestDTO {
    private String family;
    private String pipeline;

    public GetImgAI_HealthCheckRequestDTO(String family, String pipeline) {
        this.family = family;
        this.pipeline = pipeline;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getPipeline() {
        return pipeline;
    }

    public void setPipeline(String pipeline) {
        this.pipeline = pipeline;
    }
}
