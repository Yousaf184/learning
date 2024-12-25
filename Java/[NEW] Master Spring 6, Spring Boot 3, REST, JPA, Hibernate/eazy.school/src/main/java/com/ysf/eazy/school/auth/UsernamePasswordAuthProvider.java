package com.ysf.eazy.school.auth;

import com.ysf.eazy.school.dao.jpa.PersonRepository;
import com.ysf.eazy.school.model.jpa.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UsernamePasswordAuthProvider implements AuthenticationProvider {

    private final PersonRepository personRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsernamePasswordAuthProvider(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userEmail = authentication.getName();
        String userPassword = authentication.getCredentials().toString();

        Person person = this.personRepository.findByEmail(userEmail);

        if (person != null && this.passwordEncoder.matches(userPassword, person.getPassword())) {
            List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
            grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_" + person.getRole().getRoleName()));

            return new UsernamePasswordAuthenticationToken(person.getName(), person.getPassword(), grantedAuthorityList);
        } else {
            throw new BadCredentialsException("Invalid username/password combination");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
