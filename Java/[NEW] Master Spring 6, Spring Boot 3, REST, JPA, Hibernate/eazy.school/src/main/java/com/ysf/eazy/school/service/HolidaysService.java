package com.ysf.eazy.school.service;

import com.ysf.eazy.school.dao.HolidaysRepository;
import com.ysf.eazy.school.model.Holiday;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HolidaysService {

    private final HolidaysRepository holidaysRepository;

    @Autowired
    public HolidaysService(HolidaysRepository holidaysRepository) {
        this.holidaysRepository = holidaysRepository;
    }

    public List<Holiday> getAllHolidays() {
        return this.holidaysRepository.getAllHolidays();
    }
}
