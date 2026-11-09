package br.com.vscgabriel.baozistore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "br.com.vscgabriel.baozistore")
public class BaoziStoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(BaoziStoreApplication.class, args);
    }
}
