package pe.com.sikalma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SikalmaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SikalmaApplication.class, args);
    }
}
