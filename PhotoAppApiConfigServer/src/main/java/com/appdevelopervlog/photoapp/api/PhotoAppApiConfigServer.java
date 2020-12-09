package com.appdevelopervlog.photoapp.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class PhotoAppApiConfigServer {

    public static void main(String[] args) {
        SpringApplication.run(PhotoAppApiConfigServer.class, args);
    }

}
