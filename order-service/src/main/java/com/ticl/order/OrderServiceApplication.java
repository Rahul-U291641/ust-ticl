package com.ticl.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(
        scanBasePackages = {
                "com.ticl.order",
                "com.ticl.commons"
        }
)
@EnableJpaRepositories(basePackages = {
        "com.ticl.order",
        "com.ticl.commons.repository"
})
@EntityScan(basePackages = {
        "com.ticl.order",
        "com.ticl.commons.entity"
})public class OrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

}
