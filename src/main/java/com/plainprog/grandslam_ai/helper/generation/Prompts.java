package com.plainprog.grandslam_ai.helper.generation;

import com.plainprog.grandslam_ai.object.constant.images.ImgGenModuleId;

public class Prompts {
    /**
     * [Covered with]: PromptsExistenceTest
     */
    public static String positivePromptCustomization(int moduleId, String userPrompt){
        if (moduleId == ImgGenModuleId.REALISTIC_PORTRAIT){
            return "Photograph of " + userPrompt + ", shot on iphone, dslr, photorealistic, raw, 4k, (8K UHD:1.2), (hyperrealism:1.2), capturing every detail, portrait shot, vibrant colors";
        } else if (moduleId == ImgGenModuleId.NEBULA){
            String customPart = "ghibli ,(1girl ),lip, hairpin,custard, copper gradient background, Psychedelic hair,Short bob hair, Supple ,Contemporary ,Dusan Djukaric, (masterpiece,best quality,niji style)";
            return customPart + ", " + userPrompt;
        } else if (moduleId == ImgGenModuleId.OCTANE){
            String customPart = "High end digital art, octane render, unreal engine 5, volumetric lighting, 8k uhd, photorealistic, highly detailed, masterpiece, vibrant, V-ray, ray traced, gorgeous, depth of field";
            return customPart + ", " + userPrompt;
        } else if (moduleId == ImgGenModuleId.MACRO){
            String customPart = "macro shot, cinematic photo, dark shot, film grain, extremely detailed, perfect lighting";
            return customPart + ", " + userPrompt;
        } else if (moduleId == ImgGenModuleId.AESTHETIC){
            String customPart = "Perfect lighting, vibrant, high detailed, epic, aesthetic, elegant background, exquisite design, ideal shapes, expensive materials, hard worked textures, shiny surfaces, sophisticated objects";
            return "cinematic photo of  " + userPrompt + ". " + customPart;
        } else if (moduleId == ImgGenModuleId.REALISM_ART){
            String customPart =  "realism art, Gustave Courbet, Jean-François Millet, Edward Hopper, painting";
            return customPart + ", " + userPrompt;
        } else if (moduleId == ImgGenModuleId.IMPRESSIONISM_ART){
            String customPart =  "impressionism art, Claude Monet, Pierre-Auguste Renoir, Edgar Degas, painting";
            return customPart + ", " + userPrompt;
        } else if (moduleId == ImgGenModuleId.EXPRESSIONISM_ART){
            String customPart =  "expressionism art, Edvard Munch, Egon Schiele, Wassily Kandinsky, painting";
            return customPart + ", " + userPrompt;
        } else if (moduleId == ImgGenModuleId.SURREALISM_ART){
            String customPart =  "surrealism art, Salvador Dalí, René Magritte, Max Ernst, painting";
            return customPart + ", " + userPrompt;
        } else if (moduleId == ImgGenModuleId.ABSTRACT_ART){
            String customPart =  "abstract art, Jackson Pollock, Mark Rothko, Piet Mondrian, painting";
            return customPart + ", " + userPrompt;
        } else if (moduleId == ImgGenModuleId.CUBISM_ART){
            String customPart =  "cubism art, Pablo Picasso, Georges Braque, Juan Gris, painting";
            return customPart + ", " + userPrompt;
        } else if (moduleId == ImgGenModuleId.ART_NOUVEAU_ART){
            String customPart =  "Art Nouveau, Alphonse Mucha, Gustav Klimt, Antoni Gaudí, painting";
            return customPart + ", " + userPrompt;
        } else if (moduleId == ImgGenModuleId.GOTHIC_ART){
            String customPart =  "Gothic art, Hieronymus Bosch, Matthias Grünewald, Jan van Eyck, painting";
            return customPart + ", " + userPrompt;
        } else if (moduleId == ImgGenModuleId.DADAISM_ART){
            String customPart =  "Dadaism art, Marcel Duchamp, Tristan Tzara, Hannah Höch, painting";
            return customPart + ", " + userPrompt;
        } else if (moduleId == ImgGenModuleId.NEOCLASSICISM_ART){
            String customPart =  "Neo-Classicism art, Jacques-Louis David, Jean-Auguste-Dominique Ingres, Antonio Canova, painting";
            return customPart + ", " + userPrompt;
        } else if (moduleId == ImgGenModuleId.ROMANTICISM_ART){
            String customPart =  "Romanticism art, Francisco Goya, J.M.W. Turner, Caspar David Friedrich, painting";
            return customPart + ", " + userPrompt;
        } else if (moduleId == ImgGenModuleId.POST_IMPRESSIONISM_ART){
            String customPart =  "Post-Impressionism, Vincent van Gogh, Paul Cézanne, Paul Gauguin, painting";
            return customPart + ", " + userPrompt;
        } else if (moduleId == ImgGenModuleId.PIXEL_ART){
            String customPart = "pixel art. image of ";
            return customPart + " " + userPrompt;
        } else if (moduleId == ImgGenModuleId.VECTOR_ART){
            String customPart = "digital image. minimalistic vector art. masterpiece. image of";
            return customPart + " " + userPrompt;
        } else if (moduleId == ImgGenModuleId.DIGITAL_PHOTOGRAPHY){
            String customPart = "digital photography artwork. imaginary concepts, creative blend of objects. minimalistic style";
            return userPrompt + ", " + customPart;
        } else if (moduleId == ImgGenModuleId.ACID){
            String customPart = "(masterpiece:1.1),(highest quality:1.1),(HDR:1),ambient light,ultra-high quality,( ultra detailed original illustration), double exposure, fusion of fluid abstract art,glitch, acid, surreal,(original illustration composition),(fusion of limited color, maximalism artstyle, geometric artstyle, junk art)";
            return "illustration of  " + userPrompt + ". " + customPart;
        } else if (moduleId == ImgGenModuleId.LINE_ART){
            String customPart =  "one-line art, single line art, extremely minimalistic, minimalistic line drawing of ";
            return customPart + " " + userPrompt;
        }
        throw new IllegalArgumentException("Invalid module id");
    }
    /**
     * [Covered with]: PromptsExistenceTest
     */
    public static String negativePrompt(int moduleId){
        if (moduleId == ImgGenModuleId.REALISTIC_PORTRAIT){
            return "(octane render, render, drawing, anime, bad photo, bad photography:1.3),(worst quality, low quality, illustration, 3d, 2d, painting, cartoons, sketch, disney, doll, animation, comic, cropped, out of frame, low res, jpeg), (open mouth, bad teeth, deformed teeth, deformed lips), (bad anatomy, bad proportions:1.1), (deformed iris, deformed pupils, Cellulite), (deformed eyes, bad eyes), (deformed face, ugly face, bad face), (deformed hands, bad hands, fused fingers), morbid, mutilated, mutation, disfigured";
        } else if (moduleId == ImgGenModuleId.NEBULA){
            return "(worst quality, low quality,realistic,illustration, 3d, 2d, painting, cartoons, sketch)";
        } else if (moduleId == ImgGenModuleId.OCTANE){
            return "anime, cartoon, 3d model, western fantasy, blurry, noisy, low resolution, poor quility";
        } else if (moduleId == ImgGenModuleId.MACRO){
            return "(octane render, render, drawing, anime),(illustration, 3d, 2d, painting, cartoons, sketch, disney, doll, animation, comic), (open mouth, bad teeth, deformed teeth, deformed lips), (bad anatomy, bad proportions:1.1), (deformed iris, deformed pupils, Cellulite), (deformed eyes, bad eyes), (deformed face, ugly face, bad face), (deformed hands, bad hands, fused fingers), morbid, mutilated, mutation, disfigured";
        } else if (moduleId == ImgGenModuleId.AESTHETIC) {
            return "(octane render, render, drawing, anime),(illustration, 3d, 2d, painting, cartoons, sketch, disney, doll, animation, comic), (open mouth, bad teeth, deformed teeth, deformed lips), (bad anatomy, bad proportions:1.1), (deformed iris, deformed pupils), (deformed eyes, bad eyes), (deformed face, ugly face, bad face), (deformed hands, bad hands, fused fingers), morbid, mutilated, mutation, disfigured";
        } else if (moduleId == ImgGenModuleId.REALISM_ART){
            return "(photo, high-quality, image, bad painting, ultrarealistic, octane render, render),(illustration, 3d, 2d, cartoons, disney, doll, animation, comic), (open mouth, bad teeth, deformed teeth, deformed lips), (bad anatomy, bad proportions:1.1), (deformed iris, deformed pupils), (deformed eyes, bad eyes), (deformed face, ugly face, bad face), (deformed hands, bad hands, fused fingers), morbid, mutilated, mutation, disfigured";
        } else if (moduleId == ImgGenModuleId.IMPRESSIONISM_ART){
            return "(photo, high-quality, image, bad painting, ultrarealistic, octane render, render),(illustration, 3d, 2d, cartoons, disney, doll, animation, comic), (open mouth, bad teeth, deformed teeth, deformed lips), (bad anatomy, bad proportions:1.1), (deformed iris, deformed pupils), (deformed eyes, bad eyes), (deformed face, ugly face, bad face), (deformed hands, bad hands, fused fingers), morbid, mutilated, mutation, disfigured";
        } else if (moduleId == ImgGenModuleId.EXPRESSIONISM_ART){
            return "(photo, high-quality, image, bad painting, ultrarealistic, octane render, render),(illustration, 3d, 2d, cartoons, disney, doll, animation, comic), (open mouth, bad teeth, deformed teeth, deformed lips), (bad anatomy, bad proportions:1.1), (deformed iris, deformed pupils), (deformed eyes, bad eyes), (deformed face, ugly face, bad face), (deformed hands, bad hands, fused fingers), morbid, mutilated, mutation, disfigured";
        } else if (moduleId == ImgGenModuleId.SURREALISM_ART){
            return "(photo, high-quality, image, bad painting, ultrarealistic, octane render, render),(illustration, 3d, 2d, cartoons, disney, doll, animation, comic), (open mouth, bad teeth, deformed teeth, deformed lips), (bad anatomy, bad proportions:1.1), (deformed iris, deformed pupils), (deformed eyes, bad eyes), (deformed face, ugly face, bad face), (deformed hands, bad hands, fused fingers), morbid, mutilated, mutation, disfigured";
        } else if (moduleId == ImgGenModuleId.ABSTRACT_ART){
            return "(photo, high-quality, image, bad painting, ultrarealistic, octane render, render),(illustration, 3d, 2d, cartoons, disney, doll, animation, comic), (open mouth, bad teeth, deformed teeth, deformed lips), (bad anatomy, bad proportions:1.1), (deformed iris, deformed pupils), (deformed eyes, bad eyes), (deformed face, ugly face, bad face), (deformed hands, bad hands, fused fingers), morbid, mutilated, mutation, disfigured";
        } else if (moduleId == ImgGenModuleId.CUBISM_ART){
            return "(photo, high-quality, image, bad painting, ultrarealistic, octane render, render),(illustration, 3d, 2d, cartoons, disney, doll, animation, comic), (open mouth, bad teeth, deformed teeth, deformed lips), (bad anatomy, bad proportions:1.1), (deformed iris, deformed pupils), (deformed eyes, bad eyes), (deformed face, ugly face, bad face), (deformed hands, bad hands, fused fingers), morbid, mutilated, mutation, disfigured";
        } else if (moduleId == ImgGenModuleId.ART_NOUVEAU_ART){
            return "(photo, high-quality, image, bad painting, ultrarealistic, octane render, render),(illustration, 3d, 2d, cartoons, disney, doll, animation, comic), (open mouth, bad teeth, deformed teeth, deformed lips), (bad anatomy, bad proportions:1.1), (deformed iris, deformed pupils), (deformed eyes, bad eyes), (deformed face, ugly face, bad face), (deformed hands, bad hands, fused fingers), morbid, mutilated, mutation, disfigured";
        } else if (moduleId == ImgGenModuleId.GOTHIC_ART){
            return "(photo, high-quality, image, bad painting, ultrarealistic, octane render, render),(illustration, 3d, 2d, cartoons, disney, doll, animation, comic), (open mouth, bad teeth, deformed teeth, deformed lips), (bad anatomy, bad proportions:1.1), (deformed iris, deformed pupils), (deformed eyes, bad eyes), (deformed face, ugly face, bad face), (deformed hands, bad hands, fused fingers), morbid, mutilated, mutation, disfigured";
        } else if (moduleId == ImgGenModuleId.DADAISM_ART){
            return "(photo, high-quality, image, bad painting, ultrarealistic, octane render, render),(illustration, 3d, 2d, cartoons, disney, doll, animation, comic), (open mouth, bad teeth, deformed teeth, deformed lips), (bad anatomy, bad proportions:1.1), (deformed iris, deformed pupils), (deformed eyes, bad eyes), (deformed face, ugly face, bad face), (deformed hands, bad hands, fused fingers), morbid, mutilated, mutation, disfigured";
        } else if (moduleId == ImgGenModuleId.NEOCLASSICISM_ART){
            return "(photo, high-quality, image, bad painting, ultrarealistic, octane render, render),(illustration, 3d, 2d, cartoons, disney, doll, animation, comic), (open mouth, bad teeth, deformed teeth, deformed lips), (bad anatomy, bad proportions:1.1), (deformed iris, deformed pupils), (deformed eyes, bad eyes), (deformed face, ugly face, bad face), (deformed hands, bad hands, fused fingers), morbid, mutilated, mutation, disfigured";
        } else if (moduleId == ImgGenModuleId.ROMANTICISM_ART){
            return "(photo, high-quality, image, bad painting, ultrarealistic, octane render, render),(illustration, 3d, 2d, cartoons, disney, doll, animation, comic), (open mouth, bad teeth, deformed teeth, deformed lips), (bad anatomy, bad proportions:1.1), (deformed iris, deformed pupils), (deformed eyes, bad eyes), (deformed face, ugly face, bad face), (deformed hands, bad hands, fused fingers), morbid, mutilated, mutation, disfigured";
        } else if (moduleId == ImgGenModuleId.POST_IMPRESSIONISM_ART){
            return "(photo, high-quality, image, bad painting, ultrarealistic, octane render, render),(illustration, 3d, 2d, cartoons, disney, doll, animation, comic), (open mouth, bad teeth, deformed teeth, deformed lips), (bad anatomy, bad proportions:1.1), (deformed iris, deformed pupils), (deformed eyes, bad eyes), (deformed face, ugly face, bad face), (deformed hands, bad hands, fused fingers), morbid, mutilated, mutation, disfigured";
        } else if (moduleId == ImgGenModuleId.PIXEL_ART){
            return "(photo, high-quality, image, bad painting, ultrarealistic, octane render, render),(3d, 2d, cartoons, disney), morbid, mutilated, mutation, disfigured";
        } else if (moduleId == ImgGenModuleId.VECTOR_ART){
            return "(photo, high-quality, image, bad painting, ultrarealistic, octane render, render),(3d, 2d, cartoons, disney), morbid, mutilated, mutation, disfigured";
        } else if (moduleId == ImgGenModuleId.DIGITAL_PHOTOGRAPHY){
            return "(octane render, render, drawing, anime, bad photo, bad photography:1.3),(worst quality, low quality, illustration, 3d, 2d, painting, cartoons, sketch, disney, doll, animation, comic, cropped, out of frame, low res, jpeg), (open mouth, bad teeth, deformed teeth, deformed lips), (bad anatomy, bad proportions:1.1), (deformed iris, deformed pupils, Cellulite), (deformed eyes, bad eyes), (deformed face, ugly face, bad face), (deformed hands, bad hands, fused fingers), morbid, mutilated, mutation, disfigured";
        } else if (moduleId == ImgGenModuleId.ACID){
            return  "worst quality,low quality,deformed,censored,bad anatomy,watermark,signature,nsfw,explicit";
        } else if (moduleId == ImgGenModuleId.LINE_ART){
            return "(photo, high-quality, ultrarealistic, octane render, render),(3d, disney), morbid, mutilated, mutation, disfigured";
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
