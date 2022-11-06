package com.example.backend.backend.utils;

import com.example.backend.backend.config.SecurityConfigurer;
import com.google.api.client.auth.oauth2.Credential;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.Role;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.storage.Acl.User;
import com.google.common.reflect.ClassPath;


import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

@Component
public class DataBucketUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataBucketUtil.class);

    @Value("${gcp.config.file}")
    private String gcpConfigFile;

    @Value("${gcp.project.id}")
    private String gcpProjectId;

    @Value("${gcp.bucket.id}")
    private String gcpBucketId;

    @Value("${gcp.dir.name}")
    private String gcpDirectoryName;
   
    

    public String uploadFile(MultipartFile multipartFile, String fileName, String contentType) throws Exception {
          
          System.out.println(gcpConfigFile);
          System.out.println(gcpProjectId);
          System.out.println(gcpBucketId);
          System.out.println(gcpDirectoryName);
          Storage storage =StorageOptions.newBuilder().setProjectId(gcpProjectId).setCredentials(GoogleCredentials.fromStream(new FileInputStream("C:\\Users\\Kuldeep\\Desktop\\HappyPets\\backend\\src\\main\\resources\\happyPetGCP.json"))).build().getService();
          BlobId id=BlobId.of(gcpBucketId, fileName);
          BlobInfo info=BlobInfo.newBuilder(id).build();
          byte[] arr= FileUtils.readFileToByteArray(convertFile(multipartFile));
          Blob blob=storage.create(info,arr);
          System.out.println("file uploaded successfully");
          return blob.getMediaLink();

          
          
        //   try{

        //     LOGGER.debug("Start file uploading process on GCS");
        //     byte[] fileData = FileUtils.readFileToByteArray(convertFile(multipartFile));

        //     InputStream inputStream = new ClassPathResource(gcpConfigFile).getInputStream();

        //     StorageOptions options = StorageOptions.newBuilder().setProjectId(gcpProjectId)
        //             .setCredentials(GoogleCredentials.fromStream(inputStream)).build();

        //     Storage storage = options.getService();
        //     Bucket bucket = storage.get(gcpBucketId,Storage.BucketGetOption.fields());

        //     //RandomString id = new RandomString(6, ThreadLocalRandom.current());
        //     Blob blob = bucket.create(gcpDirectoryName + "/" + fileName + "-" +"kuldeep"+ checkFileExtension(fileName),fileData,contentType);

        //     if(blob != null){
        //         LOGGER.debug("File successfully uploaded to GCS");
        //         System.out.println("File successfully uploaded to GCS");
        //         return  blob.getMediaLink();
        //     }

        // }catch (Exception e){
        //     LOGGER.error("An error occurred while uploading data. Exception: ", e);
        //     throw new Exception("An error occurred while storing data to GCS");
        // }
        // throw new Exception("An error occurred while storing data to GCS");
    }

    private File convertFile(MultipartFile file) throws Exception {

        try{
            if(file.getOriginalFilename() == null){
                throw new Exception("Original file name is null");
            }
            File convertedFile = new File(file.getOriginalFilename());
            FileOutputStream outputStream = new FileOutputStream(convertedFile);
            outputStream.write(file.getBytes());
            outputStream.close();
            LOGGER.debug("Converting multipart file : {}", convertedFile);
            return convertedFile;
        }catch (Exception e){
            throw new Exception("An error has occurred while converting the file");
        }
    }

    private String checkFileExtension(String fileName) throws Exception {
        if(fileName != null && fileName.contains(".")){
            String[] extensionList = {".png", ".jpg",".jpeg", ".pdf", ".doc", ".mp3"};

            for(String extension: extensionList) {
                if (fileName.endsWith(extension)) {
                    LOGGER.debug("Accepted file type : {}", extension);
                    return extension;
                }
            }
        }
        LOGGER.error("Not a permitted file type");
        throw new Exception("Not a permitted file type");
    }
}
