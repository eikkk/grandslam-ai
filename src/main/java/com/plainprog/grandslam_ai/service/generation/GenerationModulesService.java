package com.plainprog.grandslam_ai.service.generation;

import com.plainprog.grandslam_ai.entity.img_gen.*;
import com.plainprog.grandslam_ai.helper.generation.GetImgAI;
import com.plainprog.grandslam_ai.helper.image.ImgGenModuleSortOrder;
import com.plainprog.grandslam_ai.helper.image.ModuleExamples;
import com.plainprog.grandslam_ai.object.constant.images.ProviderId;
import com.plainprog.grandslam_ai.object.dto.image.GetImgAI_HealthCheckRequestDTO;
import com.plainprog.grandslam_ai.object.response_models.generation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GenerationModulesService {

    @Autowired
    private ImgGenModuleRepository imgGenModuleRepository;
    @Autowired
    private ImgGenGroupRepository imgGenGroupRepository;

    public ImgGenModulesResponse getModules(){
        Set<Integer> groupIds = new HashSet<>();
        Map<Integer, List<ImgGenModuleModel>> groupModuleMap = new HashMap<>();
        Map<Integer, ImgGenGroup> groupMap = new HashMap<>();
        List<ImgGenModule> modules = imgGenModuleRepository.findAll().stream().filter(ImgGenModule::getActive).toList();
        for (ImgGenModule module : modules){
            if (module.getGroup() == null || !module.getGroup().getActive()){
                continue;
            }
            List<ModuleExamples.ExampleEntry> examples = ModuleExamples.getExamplesByModuleId(module.getId());

            ImgGenModuleModel moduleModel = new ImgGenModuleModel(
                    module.getId(),
                    module.getName(),
                    module.getProvider(),
                    examples.stream().map(ex -> ex.image).collect(Collectors.toList()),
                    examples,
                    module.getGroup().getId(),
                    module.getGroup().getName());


            // if not already added, add group to map
            if (!groupModuleMap.containsKey(module.getGroup().getId())){
                groupModuleMap.put(module.getGroup().getId(), new ArrayList<>(List.of(moduleModel)));
                groupMap.put(module.getGroup().getId(), module.getGroup());
                groupIds.add(module.getGroup().getId());
            } else {
                groupModuleMap.get(module.getGroup().getId()).add(moduleModel);
            }
        }

        List<ImgGenGroupModel> groupModels = new ArrayList<>();
        for (int groupId : groupIds){
            ImgGenGroup group = groupMap.get(groupId);
            List<ImgGenModuleModel> groupModules = groupModuleMap.get(groupId);
            List<Integer> modulesSortOrder = ImgGenModuleSortOrder.modulesOrder.get(groupId);
            groupModules.sort(Comparator.comparingInt(moduleModel -> modulesSortOrder.indexOf(moduleModel.getId())));
            groupModels.add(new ImgGenGroupModel(groupId, group.getName(), groupModules));
        }
        List<Integer> groupIdOrder = ImgGenModuleSortOrder.groupIdOrder;
        groupModels.sort(Comparator.comparingInt(groupModel -> groupIdOrder.indexOf(groupModel.getGroupId())));

        return new ImgGenModulesResponse(groupModels);
    }

    public ModulesHealthCheckResponse healthCheckModules() {
        List<ImgGenModule> deactivatedModules = new ArrayList<>();
        List<GetImgAI_Model> models;
        try {
            GetImgAI_ModelsResponse stableDiffusionModels = GetImgAI.getModels(new GetImgAI_HealthCheckRequestDTO("stable-diffusion-xl", "text-to-image"));
            models = stableDiffusionModels.getModels();
        } catch (Exception e) {
            return new ModulesHealthCheckResponse(new ArrayList<>(), "Error during health check: " + e.getMessage());
        }

        List<ImgGenModule> modules = imgGenModuleRepository.findAll().stream().filter(ImgGenModule::getActive).toList();
        List<Integer> providerIds = modules.stream().map(module -> module.getProvider().getId()).distinct().toList();


        Map<Integer, Boolean> moduleStatusMap = new HashMap<>();
        for (Integer providerId : providerIds) {
            String providerStringId = ProviderId.toModelName(providerId);
            List<ImgGenModule> providerModules = modules.stream().filter(module -> module.getProvider().getId().equals(providerId)).toList();
            boolean isProviderAvailable = models.stream().anyMatch(model -> model.getId().equals(providerStringId));
            for (ImgGenModule module : providerModules) {
                moduleStatusMap.put(module.getId(), isProviderAvailable);
            }
        }

        List<ImgGenModule> notAvailableModules = new ArrayList<>();
        for (ImgGenModule module : modules) {
            if (!moduleStatusMap.get(module.getId())) {
                notAvailableModules.add(module);
            }
        }

        for (ImgGenModule module : notAvailableModules) {
            module.setActive(false);
            imgGenModuleRepository.save(module);
            deactivatedModules.add(module);
        }

        if (deactivatedModules.isEmpty()) {
            return new ModulesHealthCheckResponse(new ArrayList<>(), "All modules are active.");
        } else {
            return new ModulesHealthCheckResponse(deactivatedModules, "Some modules were deactivated.");
        }
    }
}
