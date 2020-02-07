package ftn.tim16.ClinicalCentreSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableScheduling
@SpringBootApplication
@EnableTransactionManagement
@EnableAsync
public class ClinicalCentreSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClinicalCentreSystemApplication.class, args);
    }

}
