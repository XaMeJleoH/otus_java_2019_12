package ru.otus.hw;

import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.otus.hw.socket.MSSocketServer;

@AllArgsConstructor
@EnableConfigurationProperties(ConnectionProperties.class)
@SpringBootApplication
public class Application implements ApplicationRunner {
    private MSSocketServer msSocketServer;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        msSocketServer.initializeServer();
    }
}
