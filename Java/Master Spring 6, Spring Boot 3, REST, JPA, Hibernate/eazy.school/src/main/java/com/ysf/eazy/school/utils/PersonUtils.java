package com.ysf.eazy.school.utils;

import com.ysf.eazy.school.model.jpa.Person;

import java.util.Objects;

public class PersonUtils {
    public static String validateStudentBeforeAddingInClass(Person studentToBeAdded, Integer classId) {
        String errorMsg = null;

        if (studentToBeAdded == null) {
            errorMsg = "Student with given email not found";
        } else if (studentToBeAdded.getEazyClass() != null) {
            String email = studentToBeAdded.getEmail();

            if (studentToBeAdded.getEazyClass().getId().equals(classId)) {
                errorMsg = "Student (" + email + ") is already part of this class";
            } else {
                String studentClassName = studentToBeAdded.getEazyClass().getName();
                errorMsg = "Student (" + email + ") is already part of another class: " + studentClassName;
            }
        }

        return errorMsg;
    }

    public static String validateStudentBeforeRemovingFromClass(Person studentToBeRemoved, Integer classId) {
        String errorMsg = null;

        if (
            studentToBeRemoved == null ||
            studentToBeRemoved.getEazyClass() == null ||
            !studentToBeRemoved.getEazyClass().getId().equals(classId)
        ) {
            errorMsg = "Student is not part of this class";
        }

        return errorMsg;
    }

    public static String validateStudentBeforeAddingInCourse(Person studentToBeAdded, Integer courseId) {
        String errorMsg = null;

        if (studentToBeAdded == null) {
            errorMsg = "Student with given email not found";
        } else if (studentToBeAdded.getCourses() != null) {
            boolean isStudentAlreadyEnrolled = studentToBeAdded.getCourses()
                    .stream().anyMatch(course -> course.getId().equals(courseId));

            if (isStudentAlreadyEnrolled) {
                errorMsg = "Student (" + studentToBeAdded.getEmail() + ") is already part of this course";
            }
        }

        return errorMsg;
    }

    public static String validateStudentBeforeRemovingFromCourse(Person studentToBeRemoved, Integer courseId) {
        String errorMsg = null;

        if (
            studentToBeRemoved == null ||
            studentToBeRemoved.getCourses() == null ||
            studentToBeRemoved.getCourses().stream().noneMatch(course -> Objects.equals(course.getId(), courseId))
        ) {
            errorMsg = "Student is not part of this course";
        }

        return errorMsg;
    }
}
