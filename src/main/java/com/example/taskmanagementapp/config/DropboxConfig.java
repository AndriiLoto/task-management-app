package com.example.taskmanagementapp.config;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.oauth.DbxCredential;
import com.dropbox.core.v2.DbxClientV2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DropboxConfig {

    @Bean
    public DbxClientV2 dbxClientV2(@Value("${dropbox.app-key}") String appKey,
                                   @Value("${dropbox.app-secret}") String appSecret,
                                   @Value("${dropbox.refresh-token}") String refreshToken) {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("TaskManagementApp/1.0").build();
        DbxCredential credential = new DbxCredential("",0L,refreshToken,appKey,appSecret);
        return new DbxClientV2(config, credential);
    }
}
