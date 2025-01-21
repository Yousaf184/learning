package com.ysf.eazy.school.model.jpa;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
@NoArgsConstructor
@Getter
@Setter
public class Course extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "fees")
    private String fees;

    @ManyToMany(mappedBy = "courses")
    private Set<Person> students = new HashSet<>();
}
