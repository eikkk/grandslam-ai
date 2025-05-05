package com.plainprog.grandslam_ai.service.generation;

import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.entity.img_gen.ImgGenModule;
import com.plainprog.grandslam_ai.helper.generation.Prompts;
import com.plainprog.grandslam_ai.object.constant.images.ImgGenModuleId;
import com.plainprog.grandslam_ai.object.request_models.generation.ImgGenRequest;
import com.plainprog.grandslam_ai.object.response_models.generation.ImgGenModuleModel;
import com.plainprog.grandslam_ai.object.response_models.generation.ImgGenResponse;
import com.plainprog.grandslam_ai.service.account.helper.TestUserHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestGenerationHelper {
    @Autowired
    private ImageGenerationService imageGenerationService;
    @Autowired
    private GenerationModulesService generationModulesService;
    @Autowired
    private TestUserHelper testUserHelper;
    public ImgGenResponse produceTestUserImage() throws Exception {
        var modules = generationModulesService.getActiveModules().getGroups()
                .stream()
                .flatMap(group -> group.getModules().stream())
                .toList();
        //get any custom module
        ImgGenModuleModel testModule = modules.stream().filter(m -> !ImgGenModuleId.RAW_MODEL_MODULES.contains(m.getId())).findFirst().orElse(null);
        if (testModule == null) {
            throw new RuntimeException("No test module found");
        }
        ImgGenRequest imgGenRequest = new ImgGenRequest(Prompts.testPrompt, "s", testModule.getProvider().getId(), testModule.getId());
        Account testUser = testUserHelper.ensureTestUserExists();
        return imageGenerationService.generateImage(imgGenRequest, testUser, false, 0, testModule.getProvider().getId());
    }
}
