package com.ysf.eazy.school.dao.jpa;

import com.ysf.eazy.school.model.jpa.Person;
import com.ysf.eazy.school.model.jpa.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    Person findByEmail(String email);
    Person findByEmailAndRole(String email, Role role);
    Person findByIdAndRole(Integer id, Role role);
}
