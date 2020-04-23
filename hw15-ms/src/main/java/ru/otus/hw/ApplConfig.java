package ru.otus.hw;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.hibernate.SessionFactory;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import ru.otus.hw.db.model.Address;
import ru.otus.hw.db.model.Phone;
import ru.otus.hw.db.model.User;
import ru.otus.hw.hibernate.HibernateUtils;

import java.time.format.DateTimeFormatter;

@Configuration
@EnableWebSocketMessageBroker
@EnableScheduling
public class ApplConfig implements WebSocketMessageBrokerConfigurer {
  public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker("/topic");
    config.setApplicationDestinationPrefixes("/app");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/gs-guide-websocket").withSockJS();
  }


  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
    return builder -> {
      builder.simpleDateFormat(DATE_TIME_FORMAT);
      builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
    };
  }

  @Bean
  public SessionFactory sessionFactory() {
    return HibernateUtils.buildSessionFactory("hibernate.cfg.xml",
            User.class, Address.class, Phone.class);
  }

}
