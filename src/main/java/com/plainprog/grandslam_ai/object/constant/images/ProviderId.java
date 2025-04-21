package com.plainprog.grandslam_ai.object.constant.images;


import java.util.List;

public final class ProviderId {
    public static final int STABLE_DIFFUSION_XL = 1;
    public static final int REALVIS_XL_4 = 2;

    public static List<Integer> SDXL_Providers(){
        return List.of(STABLE_DIFFUSION_XL, REALVIS_XL_4);
    }

    public static String toModelName(int providerId){
        if (providerId == STABLE_DIFFUSION_XL) {
            return "stable-diffusion-xl-v1-0";
        } else if (providerId == REALVIS_XL_4) {
            return "realvis-xl-v4";
        }
        throw new IllegalArgumentException("Invalid provider id");
    }
}
