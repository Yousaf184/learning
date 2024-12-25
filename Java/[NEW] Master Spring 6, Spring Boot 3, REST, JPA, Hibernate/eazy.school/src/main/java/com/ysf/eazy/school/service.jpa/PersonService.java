package com.ysf.eazy.school.service.jpa;

import com.ysf.eazy.school.dao.jpa.PersonRepository;
import com.ysf.eazy.school.dao.jpa.RoleRepository;
import com.ysf.eazy.school.model.jpa.Person;
import com.ysf.eazy.school.model.jpa.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PersonService(PersonRepository personRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean registerUser(Person newUser) {
        Role studentRole = this.roleRepository.findByRoleName("STUDENT");
        newUser.setRole(studentRole);

        String hashedPassword = this.passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(hashedPassword);

        Person savedPerson = this.personRepository.save(newUser);

        return savedPerson.getId() != null;
    }
}
