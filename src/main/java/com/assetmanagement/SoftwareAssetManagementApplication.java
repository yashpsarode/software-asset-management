package com.assetmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SoftwareAssetManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoftwareAssetManagementApplication.class, args);
    }
}
