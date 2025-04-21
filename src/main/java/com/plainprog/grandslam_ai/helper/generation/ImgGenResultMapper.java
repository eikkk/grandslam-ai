package com.plainprog.grandslam_ai.helper.generation;

import com.plainprog.grandslam_ai.object.dto.image.ImgGenCommonResult;
import com.plainprog.grandslam_ai.object.response_models.generation.GetImgAI_StableDiffusionResponse;

public class ImgGenResultMapper {
    public static ImgGenCommonResult mapToCommonResult(GetImgAI_StableDiffusionResponse response) {
        return new ImgGenCommonResult(response.getUrl(), response.getCost(), response.getSeed());
    }
}
