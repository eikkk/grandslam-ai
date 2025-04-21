package com.plainprog.grandslam_ai.service.generation;

import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.entity.img_gen.Image;
import com.plainprog.grandslam_ai.entity.img_gen.ImageRepository;
import com.plainprog.grandslam_ai.entity.img_gen.ImgGenModule;
import com.plainprog.grandslam_ai.entity.img_gen.ImgGenProvider;
import com.plainprog.grandslam_ai.helper.generation.GetImgAI;
import com.plainprog.grandslam_ai.helper.generation.Prompts;
import com.plainprog.grandslam_ai.helper.image.ImageCompressor;
import com.plainprog.grandslam_ai.object.constant.images.ImgGenModuleId;
import com.plainprog.grandslam_ai.object.constant.images.ProviderId;
import com.plainprog.grandslam_ai.object.dto.image.ImageDTO;
import com.plainprog.grandslam_ai.object.dto.image.ImgGenCommonResult;
import com.plainprog.grandslam_ai.object.dto.util.Size;
import com.plainprog.grandslam_ai.object.request_models.generation.GetImgAI_StableDiffusionRequest;
import com.plainprog.grandslam_ai.object.request_models.generation.ImgGenRequest;
import com.plainprog.grandslam_ai.object.response_models.generation.ImgGenResponse;
import com.plainprog.grandslam_ai.service.gcp.GCPStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Instant;

@Service
public class ImageGenerationService {

    @Autowired
    private GCPStorageService storageService;
    @Autowired
    private ImageRepository imageRepository;

    public ImgGenResponse generateImage(ImgGenRequest request, Account account) throws Exception {
        //calculate size from orientation string
        int width = 1280;
        int height = 1280;
        if (request.getOrientation().equalsIgnoreCase("v")){
            width = 864;
            height = 1536;
        } else if (request.getOrientation().equalsIgnoreCase("h")){
            width = 1536;
            height = 864;
        }
        //generate image
        ImgGenCommonResult generationResult = null;

        String positivePromptCustom = Prompts.positivePrompt(request.getModuleId());
        String negativePromptCustom = Prompts.negativePrompt(request.getModuleId());
        int steps = request.getSteps() == null || request.getSteps() <= 0 ? 25 : request.getSteps();
        if (ProviderId.SDXL_Providers().contains(request.getProviderId())){
            String modelName = ProviderId.toModelName(request.getProviderId());
            String finalPositivePrompt;
            String finalNegativePrompt = negativePromptCustom;
            if (ImgGenModuleId.BASE_MODULES().contains(request.getModuleId())){
                finalPositivePrompt = request.getPrompt();
                finalNegativePrompt = request.getNegativePrompt();
            } else if (request.getModuleId() == ImgGenModuleId.REALISTIC_PHOTO){
                finalPositivePrompt = "Photograph of " + request.getPrompt() + ", " + positivePromptCustom;
            } else {
                finalPositivePrompt = positivePromptCustom + ", " + request.getPrompt();
            }
            GetImgAI_StableDiffusionRequest r = new GetImgAI_StableDiffusionRequest(modelName, finalPositivePrompt, finalNegativePrompt, "url","jpeg",  steps, width, height);
            if (request.getSeed() != null && request.getSeed() != 0){
                r.setSeed(request.getSeed());
            }
            generationResult = GetImgAI.imageGeneration(r);
        } else {
            throw new IllegalArgumentException("Invalid module id");
        }
        //upload image to GCP and save to db
        String imageURL_String = generationResult.getUrl();
        URL imageURL = new URL(imageURL_String);
        Size sizeQCompress = new Size(width / 2, height / 2);
        Size sizeThumbnail = new Size(width / 4, width / 4);
        ImageDTO image;
        try {
            InputStream is = imageURL.openStream();
            image = upload(is, GCPStorageService.UPLOAD_GENERATION_IMAGES_FOLDER + account.getId() + "/", sizeQCompress, sizeThumbnail);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        //create img gen result
        Image dbImage = new Image(generationResult.getPrice(), request.getOrientation(), generationResult.getSeed(), image.getFullSize(), image.getCompressed(), image.getThumbnail(), Instant.now());
        ImgGenModule module = new ImgGenModule(request.getModuleId());
        ImgGenProvider provider = new ImgGenProvider(request.getProviderId());
        dbImage.setImgGenModule(module);
        dbImage.setImgGenProvider(provider);
        dbImage.setCreatorAccount(account);
        imageRepository.save(dbImage);
        return new ImgGenResponse(dbImage.getId(), image, generationResult.getSeed());
    }

    private ImageDTO upload(InputStream stream, String folderPath, Size sizeForQualityCompression, Size sizeForThumbnail) throws IOException {
        long time = System.currentTimeMillis();
        BufferedImage img = ImageIO.read(stream);

        ImageCompressor compressor = new ImageCompressor();

        BufferedImage imgQ = compressor.compressWithQuality(img, sizeForQualityCompression);
        BufferedImage imgT = compressor.compressAutomatic(img, sizeForThumbnail);

        String o = storageService.uploadImage(ImageCompressor.stream(img), folderPath,"O" + time, ImageCompressor.CompressType.NONE);
        String q = storageService.uploadImage(ImageCompressor.stream(imgQ), folderPath,"Q" + time, ImageCompressor.CompressType.Q);
        String t = storageService.uploadImage(ImageCompressor.stream(imgT), folderPath, "T" + time, ImageCompressor.CompressType.T);

        return new ImageDTO(o,q,t);
    }
}
