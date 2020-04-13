package com.bld.skyeye;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})

public class SkyeyeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkyeyeApplication.class, args);
    }

}
