package com.ysf.eazy.school.model;

public record Holiday(String day, String reason, HolidayType type, DataMetaInfo metaInfo) {
    public enum HolidayType {
        FESTIVAL, FEDERAL
    }
}
