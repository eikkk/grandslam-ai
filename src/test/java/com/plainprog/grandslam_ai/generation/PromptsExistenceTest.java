package com.plainprog.grandslam_ai.generation;


import com.plainprog.grandslam_ai.entity.img_gen.ImgGenModule;
import com.plainprog.grandslam_ai.entity.img_gen.ImgGenModuleRepository;
import com.plainprog.grandslam_ai.helper.generation.Prompts;
import com.plainprog.grandslam_ai.object.constant.images.ImgGenModuleId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class PromptsExistenceTest {

    @Autowired
    private ImgGenModuleRepository imgGenModuleRepository;

    // Test to check if prompts for all live modules exist in codebase
    @Test
    public void testPromptsExistence() {
        // Get all modules from the repository
        var modules = imgGenModuleRepository.findAll().stream().filter(ImgGenModule::getActive).toList();
        var customModules = modules.stream()
                .filter(m -> !ImgGenModuleId.RAW_MODEL_MODULES.contains(m.getId()))
                .toList();
        for (var module : customModules) {
            String positivePrompt = Prompts.positivePromptCustomization(module.getId(), Prompts.testPrompt);
            assertNotNull(positivePrompt, "Prompt should not be null for module: " + module.getName());
            assertFalse(positivePrompt.isEmpty(), "Prompt should not be empty for module: " + module.getName());

            //also test for negative prompt.
            //at the moment all the custom modules require negative prompts, in future in may not be the case
            String negativePrompt = Prompts.negativePrompt(module.getId());
            assertNotNull(negativePrompt, "Negative prompt should not be null for module: " + module.getName());
            assertFalse(negativePrompt.isEmpty(), "Negative prompt should not be empty for module: " + module.getName());
        }
    }
}
