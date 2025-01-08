package com.ysf.eazy.school.model.jpa;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "class")
@Getter
@Setter
@NoArgsConstructor
public class EazyClass extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id")
    private Integer id;

    @NotBlank(message="Name must not be blank")
    @Size(min=3, message="Name must be at least 3 characters long")
    @Column(name = "name")
    private String name;

    @OneToMany(
        mappedBy = "eazyClass",
        fetch = FetchType.LAZY,
        cascade = CascadeType.PERSIST,
        orphanRemoval = true,
        targetEntity = Person.class
    )
    private Set<Person> students;

    public void addStudent(Person student) {
        this.students.add(student);
        student.setEazyClass(this);
    }

    public void removeStudent(Person student) {
        this.students.removeIf(st -> Objects.equals(st.getId(), student.getId()));
        student.setEazyClass(null);
    }

    public void removeAllStudents() {
        for (Person student : this.students) {
            student.setEazyClass(null);
        }
        this.students.clear();
    }
}
