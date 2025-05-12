package com.ysf.eazy.school.dao.jpa;

import com.ysf.eazy.school.model.jpa.Holiday;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("holidayRepositoryJPA")
public interface HolidaysRepository extends CrudRepository<Holiday, String> {

}
