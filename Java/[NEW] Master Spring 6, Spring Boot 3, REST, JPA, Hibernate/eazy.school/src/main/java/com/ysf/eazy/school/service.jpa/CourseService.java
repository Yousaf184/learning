package com.ysf.eazy.school.service.jpa;

import com.ysf.eazy.school.dao.jpa.CourseRepository;
import com.ysf.eazy.school.model.jpa.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public List<Course> getAllCourses(String sortOrder) {
        return sortOrder.equalsIgnoreCase("asc")
                ? this.courseRepository.findByOrderByName()
                : this.courseRepository.findByOrderByNameDesc();
    }

    public Course getCourseById(Integer courseId) {
        Optional<Course> courseOptional = this.courseRepository.findById(courseId);

        return courseOptional.orElseThrow(() -> {
            String message = "Course with the given id doesn't exists";
            return new IllegalArgumentException(message);
        });
    }
}
