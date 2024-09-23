package com.ysf.eazy.school.model.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "holidays")
@Getter
@Setter
@NoArgsConstructor
public class Holiday extends BaseEntity {
    public enum HolidayType {
        FESTIVAL, FEDERAL
    }

    @Id
    private String day;

    private String reason;

    @Enumerated(EnumType.STRING)
    private HolidayType type;
}
