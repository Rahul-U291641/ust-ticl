package com.ticl.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(
        scanBasePackages = {
                "com.ticl.inventory",
                "com.ticl.commons"
        }
)
@EnableJpaRepositories(basePackages = {
        "com.ticl.inventory",
        "com.ticl.commons.repository"
})
@EntityScan(basePackages = {
        "com.ticl.inventory",
        "com.ticl.commons.entity"
})
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

}
