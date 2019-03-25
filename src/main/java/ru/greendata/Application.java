/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.greendata;

import org.springframework.boot.SpringApplication;
// import org.springframework.context.annotation.ComponentScan;
// import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
// import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.context.annotation.ImportResource;
/**
 *
 * @author bronnikov-ea
 */
// @ComponentScan
// @EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@SpringBootApplication
// @ImportResource("application.properties")
public class Application {
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
