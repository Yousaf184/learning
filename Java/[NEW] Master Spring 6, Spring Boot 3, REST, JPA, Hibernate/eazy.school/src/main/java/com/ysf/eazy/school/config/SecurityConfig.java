package com.ysf.eazy.school.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrfConfigurer -> csrfConfigurer
                        .ignoringRequestMatchers("/contact/saveMsg")
                        .ignoringRequestMatchers("/user/register")
//                        .ignoringRequestMatchers(PathRequest.toH2Console()) // needed for H2 database
                )
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/dashboard").authenticated()
                        .requestMatchers("/messages").authenticated()
                        .requestMatchers("/", "/home").permitAll()
                        .requestMatchers("/holidays/**").permitAll()
                        .requestMatchers("/contact/**").permitAll()
                        .requestMatchers("/about").permitAll()
                        .requestMatchers("/courses").permitAll()
                        .requestMatchers("/assets/**").permitAll()
                        .requestMatchers("/user/register").permitAll()
                        .requestMatchers("/login").permitAll()
//                        .requestMatchers(PathRequest.toH2Console()).permitAll() // needed for H2 database
                        .anyRequest().authenticated()
                )
                .formLogin(formLoginConfigurer -> formLoginConfigurer
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard")
                        .failureUrl("/login?error=true")
                )
                .logout(logoutConfigurer -> logoutConfigurer
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                )
                .httpBasic(Customizer.withDefaults());
//                needed for H2 database
//                .headers(headersConfigurer -> headersConfigurer
//                        // to allow opening h2-console
//                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
//                );

        return httpSecurity.build();
    }

//    @Bean
//    @SuppressWarnings("deprecation")
//    public InMemoryUserDetailsManager userDetailsManager() {
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("12345")
//                .roles("USER")
//                .build();
//
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("admin")
//                .password("12345")
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(user, admin);
//    }
}
