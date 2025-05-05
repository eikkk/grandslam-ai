package com.plainprog.grandslam_ai.object.constant.images;


import java.util.List;

public final class ProviderId {
    public static final int STABLE_DIFFUSION_XL = 1;
    public static final int REALVIS_XL_4 = 2;
    public static final int SDVN7_NIJI_STYLE_XL = 3;
    public static final int REAL_CARTOON_XL_V6 = 4;
    public static final int COUNTERFEIT_XL_V2_5 = 5;

    public static List<Integer> SDXL_Providers(){
        return List.of(STABLE_DIFFUSION_XL, REALVIS_XL_4, SDVN7_NIJI_STYLE_XL, REAL_CARTOON_XL_V6, COUNTERFEIT_XL_V2_5);
    }

    public static String toModelName(int providerId){
        if (providerId == STABLE_DIFFUSION_XL) {
            return "stable-diffusion-xl-v1-0";
        } else if (providerId == REALVIS_XL_4) {
            return "realvis-xl-v4";
        } else if (providerId == SDVN7_NIJI_STYLE_XL) {
            return "sdvn7-niji-style-xl-v1";
        } else if (providerId == REAL_CARTOON_XL_V6) {
            return "real-cartoon-xl-v6";
        } else if (providerId == COUNTERFEIT_XL_V2_5) {
            return "counterfeit-xl-v2-5";
        }
        throw new IllegalArgumentException("Invalid provider id");
    }
}
