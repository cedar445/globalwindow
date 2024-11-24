package com.zxq.globalwindow.config;

import com.aliyun.teaopenapi.models.Config;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.DefaultAcsClient;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.aliyun.tea.*;

@Configuration
public class AliyunConfig {

    //AK
    @Value("${aliyun.access-key-id}")
    private String accessKeyId;

    //SK
    @Value("${aliyun.access-key-secret}")
    private String accessKeySecret;

    @Value("${aliyun.region-id}")
    private String regionId;

    @Bean
    public DefaultAcsClient acsClient() {
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        return new DefaultAcsClient(profile);
    }

    @Bean
    public Config aliyunClientConfig() {
        Config config = new Config()
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret)
                .setEndpoint("nlp-automl.cn-hangzhou.aliyuncs.com");
        return config;
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }
}
