package com.ysf.eazy.school.service.jpa;

import com.ysf.eazy.school.dao.jpa.HolidaysRepository;
import com.ysf.eazy.school.model.jpa.Holiday;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("holidayServiceJPA")
public class HolidaysService {

    private final HolidaysRepository holidaysRepository;

    @Autowired
    public HolidaysService(@Qualifier("holidayRepositoryJPA") HolidaysRepository repo) {
        this.holidaysRepository = repo;
    }

    public List<Holiday> getAllHolidays() {
        Iterable<Holiday> holidaysIterator = this.holidaysRepository.findAll();
        return Streamable.of(holidaysIterator).toList();
    }
}
