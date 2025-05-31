package com.plainprog.grandslam_ai.service.generation;

import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.entity.img_gen.ImgGenModule;
import com.plainprog.grandslam_ai.helper.generation.Prompts;
import com.plainprog.grandslam_ai.object.constant.images.ImgGenModuleId;
import com.plainprog.grandslam_ai.object.request_models.generation.ImgGenRequest;
import com.plainprog.grandslam_ai.object.response_models.generation.ImgGenModuleModel;
import com.plainprog.grandslam_ai.object.response_models.generation.ImgGenResponse;
import com.plainprog.grandslam_ai.service.account.helper.TestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestGenerationHelper {
    @Autowired
    private ImageGenerationService imageGenerationService;
    @Autowired
    private GenerationModulesService generationModulesService;
    @Autowired
    private TestHelper testHelper;
    public ImgGenResponse produceTestUserImage() throws Exception {
        ImgGenModule module = getAnyActiveModule();
        Account testUser = testHelper.ensureTestUserExists().getAccount();
        return produceTestUserImage(module, testUser);
    }
    public ImgGenModule getAnyActiveModule(){
        var modules = generationModulesService.getActiveModules().getGroups()
                .stream()
                .flatMap(group -> group.getModules().stream())
                .toList();
        //get any custom module
        ImgGenModuleModel moduleModel = modules.stream().filter(m -> !ImgGenModuleId.RAW_MODEL_MODULES.contains(m.getId())).findFirst().orElse(null);
        if (moduleModel == null) {
            throw new RuntimeException("No active module found");
        }
        return generationModulesService.getModuleById(moduleModel.getId());
    }
    public ImgGenResponse produceTestUserImage(ImgGenModule module, Account account) throws Exception {
        if (module == null) {
            throw new RuntimeException("No test module found");
        }
        ImgGenRequest imgGenRequest = new ImgGenRequest(Prompts.testPrompt, "s", module.getProvider().getId(), module.getId());
        return imageGenerationService.generateImage(imgGenRequest, account, false, 0, module.getProvider().getId());
    }
}
