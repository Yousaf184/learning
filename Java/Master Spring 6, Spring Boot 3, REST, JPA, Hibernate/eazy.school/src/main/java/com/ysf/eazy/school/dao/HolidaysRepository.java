package com.ysf.eazy.school.dao;

import com.ysf.eazy.school.model.DataMetaInfo;
import com.ysf.eazy.school.model.Holiday;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HolidaysRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public HolidaysRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Holiday> getAllHolidays() {
        String query = "SELECT * FROM holidays";

        // Class field names and database table columns
        // should have the same name for the following to work
//        BeanPropertyRowMapper<Holiday> holidayRowMapper = BeanPropertyRowMapper.newInstance(Holiday.class);

        RowMapper<Holiday> holidayRowMapper = (rs, rowNum) -> {
            DataMetaInfo metaInfo = new DataMetaInfo(
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getString("created_by"),
                rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null,
                rs.getString("updated_by") != null ? rs.getString("updated_by") : null
            );

            return new Holiday(
                rs.getString("day"),
                rs.getString("reason"),
                Holiday.HolidayType.valueOf(rs.getString("type")),
                metaInfo
            );
        };

        return this.jdbcTemplate.query(query, holidayRowMapper);
    }
}
