package org.example;

import org.example.service.MutantDetector;
import org.example.service.MutantService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.springframework.context.ApplicationContext;

//@EntityScan("com.utn.productos.model")
//@EnableJpaRepositories("com.utn.productos.repository")
@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Main.class, args);
        System.out.println("HOLA MUNDO");

        String[] testMatrix = {
            "ATGCGA",
            "CAGTGC",
            "TTAGGT",
            "AGAAGG",
            "CCCCTA",
            "TCACTG"
        };
        MutantDetector detector = context.getBean(MutantDetector.class);
        detector.isMutant(testMatrix);
        System.out.println(detector.isMutant(testMatrix));

        MutantService servicio = context.getBean(MutantService.class);
        servicio.analyzeDna(testMatrix);
    }

}