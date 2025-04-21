package com.plainprog.grandslam_ai.helper.generation;

import com.plainprog.grandslam_ai.object.constant.images.ImgGenModuleId;

public class Prompts {
    public static String positivePrompt(int moduleId){
        if (ImgGenModuleId.RAW_MODEL_MODULES().contains(moduleId)){
            return "";
        } else if (moduleId == ImgGenModuleId.REALISTIC_PHOTO){
            return "instagram photo, ultrarealistic, aesthetic photo, cinematic shot, professional photo, photorealistic";
        }
        throw new IllegalArgumentException("Invalid module id");
    }
    public static String negativePrompt(int moduleId){
        if (ImgGenModuleId.RAW_MODEL_MODULES().contains(moduleId)){
            return "";
        } else if (moduleId == ImgGenModuleId.REALISTIC_PHOTO){
            return "(octane render, render, drawing, anime),(illustration, 3d, 2d, painting, cartoons, sketch, disney, doll, animation, comic), (open mouth, bad teeth, deformed teeth, deformed lips), (bad anatomy, bad proportions:1.1), (deformed iris, deformed pupils, Cellulite), (deformed eyes, bad eyes), (deformed face, ugly face, bad face), (deformed hands, bad hands, fused fingers), morbid, mutilated, mutation, disfigured";
        }
        throw new IllegalArgumentException("Invalid module id");
    }
    public static String forbiddenContentDetectionPrompt(String prompt){
        var base = "I need simple Yes or No answer. Nothing else.";
        String topicQuestion = "Does the following prompt contain any child abuse or child porn or pedophile content? Where child defined as less or equal 17 years old.";
        return base + " " + topicQuestion + " " + "User prompt: " + prompt;
    }
    public static String forbiddenContentSystemMessage(){
        return "You are an entity that makes decisions about content moderation.";
    }
    public static String testPrompt = "A beautiful landscape with mountains and a river, in the style of a watercolor painting.";
}
