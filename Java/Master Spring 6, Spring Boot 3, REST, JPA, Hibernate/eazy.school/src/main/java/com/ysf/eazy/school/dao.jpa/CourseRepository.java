package com.ysf.eazy.school.dao.jpa;

import com.ysf.eazy.school.model.jpa.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    // find by Name, order by Name in descending order
    List<Course> findByOrderByNameDesc();

    // find by Name, order by Name in ascending (default) order
    List<Course> findByOrderByName();
}
