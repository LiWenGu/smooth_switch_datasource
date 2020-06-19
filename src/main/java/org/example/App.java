package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Hello world!
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableAsync
public class App {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(App.class);
        ApplicationContext ctx = app.run(args);
    }
}

