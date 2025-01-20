package com.ysf.eazy.school.dao.jpa;

import com.ysf.eazy.school.model.jpa.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
}
