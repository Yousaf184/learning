package com.ysf.eazy.school;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableAspectJAutoProxy
// Following 2 annotations are only required if JPA repositories
// and entities are not placed under the main package that contains
// the main method class
//@EnableJpaRepositories("com.ysf.eazy.school")
//@EntityScan("com.ysf.eazy.school")
@EnableJpaAuditing(auditorAwareRef = "entityAuditAwareImpl")
public class EazySchoolApplication {

	public static void main(String[] args) {
		SpringApplication.run(EazySchoolApplication.class, args);
	}

}
