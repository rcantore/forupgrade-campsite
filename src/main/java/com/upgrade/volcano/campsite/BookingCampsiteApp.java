package com.upgrade.volcano.campsite;

import com.upgrade.volcano.campsite.utils.LocalDataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookingCampsiteApp implements CommandLineRunner {
    @Autowired
    LocalDataUtils localDataUtils;

    public static void main(String[] args) {
        SpringApplication.run(BookingCampsiteApp.class);
    }

    @Override
    public void run(String... args) throws Exception {
        localDataUtils.initLocalData();
    }
}
