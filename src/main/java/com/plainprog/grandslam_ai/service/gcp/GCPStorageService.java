package com.plainprog.grandslam_ai.service.gcp;


import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.plainprog.grandslam_ai.helper.image.ImageCompressor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class GCPStorageService {
    public static final String UPLOAD_GENERATION_IMAGES_FOLDER = "img_gen/";

    private final Bucket bucket;

    public GCPStorageService(@Value("${spring.cloud.gcp.storage.credentials.location}") String cred,
                             @Value("${spring.cloud.gcp.storage.images.bucket.name}") String imagesBucket) throws IOException {
        String credentialsPath = cred.split("file:")[1];
        Credentials credentials = GoogleCredentials
                .fromStream(new FileInputStream(credentialsPath));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials)
                .build().getService();
        this.bucket = storage.get(imagesBucket);
    }

    public String uploadImage(InputStream is, String folderPath, String fileName, ImageCompressor.CompressType compressType) throws IOException {
        String filePath = switch (compressType) {
            case NONE -> folderPath + "original/" + fileName + ".jpg";
            case Q -> folderPath + "q-comp/" + fileName + ".jpg";
            case T -> folderPath + "thumb/" + fileName + ".jpg";
        };
        Blob blob = bucket.create(filePath,is,"image/jpeg");
        if (blob.exists()){
            return "https://storage.googleapis.com/" + bucket.getName() + "/" + filePath;
        } else {
            return null;
        }
    }

    public boolean deleteImage(String publicURL) {
        // example of dev publicURL: "https://storage.googleapis.com/gslm-gen-dev/img_gen/10/original/O1745151108932.jpg"
        // extract the path from the URL
        String filePath = publicURL.split(bucket.getName() + "/")[1];
        Blob blob = bucket.get(filePath);
        if (blob != null && blob.exists()) {
            return blob.delete();
        } else {
            return false;
        }
    }
}