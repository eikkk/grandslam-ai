package com.plainprog.grandslam_ai.object.constant.images;

import java.util.List;

public final class ImgGenModuleId {
    public static final int STABLE_DIFFUSION_XL = 1;
    public static final int REALISTIC_PHOTO = 2;


    public static List<Integer> BASE_MODULES(){
        return List.of(STABLE_DIFFUSION_XL);
    }
}
