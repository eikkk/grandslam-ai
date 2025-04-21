package com.plainprog.grandslam_ai.helper.generation;

import com.google.gson.Gson;
import com.plainprog.grandslam_ai.object.dto.image.ImgGenCommonResult;
import com.plainprog.grandslam_ai.object.request_models.generation.GetImgAI_StableDiffusionRequest;
import com.plainprog.grandslam_ai.object.response_models.generation.GetImgAI_StableDiffusionResponse;
import com.plainprog.grandslam_ai.spring.beans.PropertiesProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class GetImgAI implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    public static ImgGenCommonResult imageGeneration(GetImgAI_StableDiffusionRequest requestBody) throws Exception {
        PropertiesProvider propsProvider = context.getBean(PropertiesProvider.class);
        String apiKey = propsProvider.getImgApiKey();
        Gson gson = new Gson();
        String body = gson.toJson(requestBody, GetImgAI_StableDiffusionRequest.class);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://api.getimg.ai/v1/stable-diffusion-xl/text-to-image"))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .headers("Content-Type", "application/json")
                .headers("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 13_2_1) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.3 Safari/605.1.15")
                .headers("Authorization", "Bearer " + apiKey)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200){
            GetImgAI_StableDiffusionResponse resp = gson.fromJson(response.body(), GetImgAI_StableDiffusionResponse.class);
            return ImgGenResultMapper.mapToCommonResult(resp);
        }
        throw new Exception("Could not generate image. Response code: " + response.statusCode());
    }
}
