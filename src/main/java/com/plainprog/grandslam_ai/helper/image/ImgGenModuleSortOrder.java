package com.plainprog.grandslam_ai.helper.image;

import com.plainprog.grandslam_ai.object.constant.images.ImgGenModuleId;

import java.util.List;
import java.util.Map;

public final class ImgGenModuleSortOrder {
    public final static List<Integer> groupIdOrder = List.of(1,2,3,4);
    public final static Map<Integer, List<Integer>> modulesOrder = Map.of(
            1, List.of(ImgGenModuleId.RAW_STABLE_DIFFUSION_XL, ImgGenModuleId.REALVIS_XL_4_RAW, ImgGenModuleId.REAL_CARTOON_XL, ImgGenModuleId.NIJI_STYLE_XL),
            2, List.of(ImgGenModuleId.AESTHETIC, ImgGenModuleId.REALISTIC_PORTRAIT, ImgGenModuleId.MACRO),
            3, List.of(ImgGenModuleId.OCTANE, ImgGenModuleId.ACID, ImgGenModuleId.DIGITAL_PHOTOGRAPHY, ImgGenModuleId.PIXEL_ART, ImgGenModuleId.VECTOR_ART, ImgGenModuleId.LINE_ART),
            4, List.of(ImgGenModuleId.IMPRESSIONISM_ART, ImgGenModuleId.EXPRESSIONISM_ART, ImgGenModuleId.SURREALISM_ART, ImgGenModuleId.ABSTRACT_ART, ImgGenModuleId.CUBISM_ART, ImgGenModuleId.ART_NOUVEAU_ART, ImgGenModuleId.GOTHIC_ART, ImgGenModuleId.REALISM_ART, ImgGenModuleId.DADAISM_ART, ImgGenModuleId.NEOCLASSICISM_ART, ImgGenModuleId.ROMANTICISM_ART, ImgGenModuleId.POST_IMPRESSIONISM_ART),
            5, List.of(ImgGenModuleId.NEBULA)
    );
}
