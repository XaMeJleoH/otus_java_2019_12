package ru.otus.hw;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "connection")
public class ConnectionProperties {

    private Ms ms = new Ms();
    private Db db = new Db();
    private Frontend frontend = new Frontend();

    @Data
    public class Ms {
        private String host;
        private int port;
    }

    @Data
    public class Db {
        private String name;
        private String host;
        private int port;
    }

    @Data
    public class Frontend {
        private String name;
        private String host;
        private int port;
    }

}
