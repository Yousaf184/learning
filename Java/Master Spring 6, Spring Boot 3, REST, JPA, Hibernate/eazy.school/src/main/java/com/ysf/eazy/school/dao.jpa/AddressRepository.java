package com.ysf.eazy.school.dao.jpa;

import com.ysf.eazy.school.model.jpa.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
}
