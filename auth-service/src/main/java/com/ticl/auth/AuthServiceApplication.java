package com.ticl.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication(
        scanBasePackages = {
                "com.ticl.auth",
                "com.ticl.commons"
        }
)
@EnableJpaRepositories(basePackages = {
        "com.ticl.auth",
        "com.ticl.commons.repository"
})
@EntityScan(basePackages = {
        "com.ticl.auth",
        "com.ticl.commons.entity"
})
public class AuthServiceApplication {

	public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
	}

}
