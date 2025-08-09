package com.ysf.accounts;

import com.ysf.accounts.dto.AccountContactInfoDto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "entityAuditorAwareImpl")
@EnableConfigurationProperties({ AccountContactInfoDto.class })
@EnableFeignClients(basePackages = { "com.ysf.accounts.client" })
public class AccountsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountsApplication.class, args);
	}

}
