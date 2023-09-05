package de.conrad.codeworkshop.factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Andreas Hartmann
 */
@SpringBootApplication
@Slf4j
public class FactoryApplication {

    public static void main(final String... args) {
        SpringApplication.run(FactoryApplication.class, args);
    }
}
