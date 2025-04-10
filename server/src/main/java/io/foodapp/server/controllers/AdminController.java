package io.foodapp.server.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    @Value("${CLOUDINARY_CLOUD_NAME}")
    private String cloudName;

    @Value("${CLOUDINARY_API_KEY}")
    private String apiKey;

    @Value("${CLOUDINARY_API_SECRET}")
    private String apiSecret;

    @GetMapping
    public String sayHello() {
        // return new String("Hello admin!!!");
        return new String(
                "Hello admin!!!" + "\n" +
                        "CLOUDINARY_CLOUD_NAME: " + cloudName + "\n" +
                        "CLOUDINARY_API_KEY: " + apiKey + "\n" +
                        "CLOUDINARY_API_SECRET: " + apiSecret + "\n");
    }

}
