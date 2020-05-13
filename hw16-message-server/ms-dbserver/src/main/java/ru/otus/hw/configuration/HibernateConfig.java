package ru.otus.hw.configuration;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import ru.otus.hw.db.model.Address;
import ru.otus.hw.db.model.Phone;
import ru.otus.hw.db.model.User;
import ru.otus.hw.hibernate.HibernateUtils;

@Configuration
@ImportResource({"classpath:hibernate.cfg.xml"})
public class HibernateConfig {

    @Bean
    public SessionFactory sessionFactory() {
        return HibernateUtils.buildSessionFactory("hibernate.cfg.xml",
                User.class, Address.class, Phone.class);
    }

}
