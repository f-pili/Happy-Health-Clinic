package it.fpili.happy_health_clinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application entry point for Happy Health Clinic.
 * Initializes and starts the Spring Boot application.
 */
@SpringBootApplication
public class HappyHealthClinicApplication {

    /**
     * Main method to start the application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(HappyHealthClinicApplication.class, args);
    }
}
