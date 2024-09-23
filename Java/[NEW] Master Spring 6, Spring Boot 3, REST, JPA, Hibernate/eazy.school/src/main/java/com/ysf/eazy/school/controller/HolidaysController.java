package com.ysf.eazy.school.controller;

import com.ysf.eazy.school.model.jpa.Holiday;
import com.ysf.eazy.school.service.jpa.HolidaysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HolidaysController {

    private final HolidaysService holidaysService;

    @Autowired
    public HolidaysController(@Qualifier("holidayServiceJPA") HolidaysService holidaysService) {
        this.holidaysService = holidaysService;
    }

    @GetMapping("/holidays")
    public String displayHolidays(
        @RequestParam(name = "festival", required = false) boolean showFestivalHolidays,
        @RequestParam(name = "federal", required = false) boolean showFederalHolidays,
        Model model
    ) {
        model.addAttribute("showFestivalHolidays", showFestivalHolidays);
        model.addAttribute("showFederalHolidays", showFederalHolidays);

        List<Holiday> holidays = this.holidaysService.getAllHolidays();

        holidays.forEach(holiday -> {
            String currentHolidayType = holiday.getType().toString();

            if (model.containsAttribute(currentHolidayType)) {
                ((List<Holiday>) model.getAttribute(currentHolidayType)).add(holiday);
            } else {
                List<Holiday> holidayList = new ArrayList<>();
                holidayList.add(holiday);
                model.addAttribute(currentHolidayType, holidayList);
            }
        });

        return "holidays.html";
    }
}
