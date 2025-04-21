package com.plainprog.grandslam_ai.object.constant.images;

import java.util.List;

public final class ImgGenModuleId {
    public static final int RAW_STABLE_DIFFUSION_XL = 1;
    public static final int REALISTIC_PHOTO = 2;


    public static List<Integer> RAW_MODEL_MODULES(){
        return List.of(RAW_STABLE_DIFFUSION_XL);
    }
}
