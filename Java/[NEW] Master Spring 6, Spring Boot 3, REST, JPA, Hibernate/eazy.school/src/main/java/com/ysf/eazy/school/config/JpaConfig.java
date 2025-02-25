package com.ysf.eazy.school.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
// Following 2 annotations are only required if JPA repositories
// and entities are not placed under the main package that contains
// the main method class
//@EnableJpaRepositories("com.ysf.eazy.school")
//@EntityScan("com.ysf.eazy.school")
@EnableJpaAuditing(auditorAwareRef = "entityAuditAwareImpl")
public class JpaConfig {
}
