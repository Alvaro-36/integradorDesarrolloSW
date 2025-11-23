package org.example;

import org.example.service.MutantDetector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@EntityScan("com.utn.productos.model")
//@EnableJpaRepositories("com.utn.productos.repository")
@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        System.out.println("HOLA MUNDO");

        String[] testMatrix = {
                "h00a",
                "00a0",
                "0a00",
                "a00s"
        };
        MutantDetector detector = MutantDetector.builder().build();
        detector.isMutant(testMatrix);


    }

}