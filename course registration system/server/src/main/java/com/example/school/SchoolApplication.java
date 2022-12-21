package com.example.school;

import com.sun.jmx.remote.internal.ArrayQueue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import springfox.documentation.oas.annotations.EnableOpenApi;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


@EnableOpenApi
@SpringBootApplication
public class SchoolApplication {
    public static void main(String[] args) {
        HashMap
        AnnotationConfigApplicationContext app = new AnnotationConfigApplicationContext("");
        SpringApplication.run(SchoolApplication.class, args);
    }
}
