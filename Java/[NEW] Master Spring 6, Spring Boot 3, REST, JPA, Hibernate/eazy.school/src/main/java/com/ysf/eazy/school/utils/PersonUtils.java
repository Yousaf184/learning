package com.ysf.eazy.school.utils;

import com.ysf.eazy.school.model.jpa.Person;

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
}
