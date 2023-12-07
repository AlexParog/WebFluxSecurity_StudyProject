package net.proselyte.webfluxsecuritysp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс приложения для запуска веб-приложения с поддержкой безопасности в реактивном стиле.
 */
@SpringBootApplication
public class WebFluxSecuritySpApplication {

    /**
     * Главный метод, запускающий веб-приложение.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        SpringApplication.run(WebFluxSecuritySpApplication.class, args);
    }

}
