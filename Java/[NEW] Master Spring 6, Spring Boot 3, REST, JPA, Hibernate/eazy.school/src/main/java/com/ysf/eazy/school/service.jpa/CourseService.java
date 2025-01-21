package com.ysf.eazy.school.service.jpa;

import com.ysf.eazy.school.dao.jpa.CourseRepository;
import com.ysf.eazy.school.model.jpa.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public void saveNewCourse(Course course) {
        this.courseRepository.save(course);
    }

    public List<Course> getAllCourses() {
        return this.courseRepository.findAll();
    }
}
