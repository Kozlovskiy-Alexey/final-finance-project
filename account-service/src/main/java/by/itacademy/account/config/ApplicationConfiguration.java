package by.itacademy.account.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
public class ApplicationConfiguration {

    @PostConstruct
    void init() {
        log.warn("App is loaded!");
    }
}
