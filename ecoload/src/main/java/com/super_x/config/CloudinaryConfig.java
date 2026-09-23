package com.super_x.config;

import java.util.HashMap;
import java.util.Map;

import com.cloudinary.Cloudinary;

public class CloudinaryConfig {

    public static Cloudinary cloudinary;
    public  static Cloudinary getCloudinary(){
        if(cloudinary == null){
            Map<String,Object> config = new HashMap<>();
            config.put("cloud_name", "cloud_name");
            config.put("api_key", "api_key");
            config.put("api_secret", "api_secret"); 

            cloudinary = new Cloudinary(config);
        }
        return cloudinary;
    }
}