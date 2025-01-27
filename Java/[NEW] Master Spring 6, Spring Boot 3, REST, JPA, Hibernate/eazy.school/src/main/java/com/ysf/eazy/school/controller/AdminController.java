package com.ysf.eazy.school.controller;

import com.ysf.eazy.school.model.jpa.Course;
import com.ysf.eazy.school.model.jpa.EazyClass;
import com.ysf.eazy.school.model.jpa.Person;
import com.ysf.eazy.school.service.jpa.CourseService;
import com.ysf.eazy.school.service.jpa.EazyClassService;
import com.ysf.eazy.school.service.jpa.PersonService;
import com.ysf.eazy.school.utils.PersonUtils;
import com.ysf.eazy.school.validation.PersonEmailValidationGroup;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final EazyClassService eazyClassService;
    private final PersonService personService;
    private final CourseService courseService;

    @Autowired
    public AdminController(
        EazyClassService eazyClassService,
        PersonService personService,
        CourseService courseService
    ) {
        this.eazyClassService = eazyClassService;
        this.personService = personService;
        this.courseService = courseService;
    }

    @GetMapping("/classes")
    public ModelAndView displayClasses() {
        ModelAndView modelAndView = new ModelAndView("classes.html");

        modelAndView.addObject("eazyClass", new EazyClass());
        modelAndView.addObject("eazyClasses", this.eazyClassService.getAllClasses());

        return modelAndView;
    }

    @PostMapping("/class")
    public String saveNewClass(
        @Valid @ModelAttribute("eazyClass") EazyClass newClass,
        Errors errors
    ) {
        if (errors.hasErrors()) {
            return "classes.html";
        }

        this.eazyClassService.saveClass(newClass);

        return "redirect:/admin/classes";
    }

    @GetMapping("/delete/class")
    public String deleteClassById(@RequestParam(name = "classId") Integer classId) {
        this.eazyClassService.deleteClassById(classId);
        return "redirect:/admin/classes";
    }

    @GetMapping("/class/students")
    public ModelAndView displayClassStudents(
        @RequestParam(name = "classId") Integer classId,
        Model model
    ) {
        ModelAndView modelAndView = new ModelAndView("students.html");
        modelAndView.addObject("person", new Person());

        EazyClass eazyClass = this.eazyClassService.getEazyClassById(classId);
        modelAndView.addObject("eazyClass", eazyClass);

        if (model.containsAttribute("success")) {
            modelAndView.addObject("success", model.getAttribute("success"));
        }

        return modelAndView;
    }

    @PostMapping("/class/student")
    public String addStudentToClass(
        @Validated(PersonEmailValidationGroup.class) @ModelAttribute("person") Person student,
        @RequestParam(name = "classId") Integer classId,
        Model model,
        RedirectAttributes redirectAttributes,
        Errors errors
    ) {
        if (errors.hasErrors()) {
            return "students.html";
        }

        Person studentToBeAdded = this.personService.getStudentByEmail(student.getEmail());
        String errorMsg = PersonUtils.validateStudentBeforeAddingInClass(studentToBeAdded, classId);

        if (errorMsg != null) {
            model.addAttribute("error", errorMsg);
            model.addAttribute("eazyClass", new EazyClass(classId));
            return "students.html";
        }

        EazyClass classToAddStudent = this.eazyClassService.getEazyClassById(classId);
        classToAddStudent.addStudent(studentToBeAdded);

        this.eazyClassService.saveClass(classToAddStudent);

        redirectAttributes.addFlashAttribute("success", "student added successfully");
        return "redirect:/admin/class/students?classId=" + classId;
    }

    @GetMapping("/class/student/remove")
    public String removeStudentFromClass(
        @RequestParam(name = "studentId") Integer studentId,
        @RequestParam(name = "classId") Integer classId,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        Person student = this.personService.getStudentById(studentId);
        String errorMsg = PersonUtils.validateStudentBeforeRemovingFromClass(student, classId);

        if (errorMsg != null) {
            model.addAttribute("errorMsg", errorMsg);
            return "students.html";
        }

        EazyClass classToRemoveFrom = this.eazyClassService.getEazyClassById(classId);
        classToRemoveFrom.removeStudent(student);

        this.eazyClassService.saveClass(classToRemoveFrom);

        String message = "Student removed successfully";
        redirectAttributes.addFlashAttribute("success", message);

        return "redirect:/admin/class/students?classId=" + classId;
    }

    @GetMapping("/courses")
    public ModelAndView displayCoursesPage(
        Model model,
        @RequestParam(name = "sort", required = false, defaultValue = "asc") String sortOrder
    ) {
        ModelAndView modelAndView = new ModelAndView("courses_secure");

        modelAndView.addObject("course", new Course());
        modelAndView.addObject("courses", this.courseService.getAllCourses(sortOrder));
        modelAndView.addObject("currentSortOrder", sortOrder);

        if (model.containsAttribute("success")) {
            modelAndView.addObject("success", model.getAttribute("success"));
        }

        return modelAndView;
    }

    @PostMapping("/course")
    public String saveNewCourse(
        @ModelAttribute("course") Course course,
        RedirectAttributes redirectAttributes
    ) {
        this.courseService.saveNewCourse(course);

        String successMsg = "Course saved successfully";
        redirectAttributes.addFlashAttribute("success", successMsg);

        return "redirect:/admin/courses?sort=asc";
    }

    @GetMapping("/course/students")
    public ModelAndView displayCourseStudents(
        @RequestParam(name = "courseId") Integer courseId,
        Model model
    ) {
        ModelAndView modelAndView = new ModelAndView("course_students.html");
        modelAndView.addObject("student", new Person());

        Course course = this.courseService.getCourseById(courseId);
        modelAndView.addObject("course", course);

        if (model.containsAttribute("success")) {
            modelAndView.addObject("success", model.getAttribute("success"));
        }

        return modelAndView;
    }

    @PostMapping("/course/student")
    public String addStudentToCourse(
        @Validated(PersonEmailValidationGroup.class) @ModelAttribute("student") Person student,
        @RequestParam(name = "courseId") Integer courseId,
        Model model,
        RedirectAttributes redirectAttributes,
        Errors errors
    ) {
        if (errors.hasErrors()) {
            return "course_students.html";
        }

        Person studentToBeAdded = this.personService.getStudentByEmail(student.getEmail());
        String errorMsg = PersonUtils.validateStudentBeforeAddingInCourse(studentToBeAdded, courseId);

        if (errorMsg != null) {
            model.addAttribute("error", errorMsg);
            model.addAttribute("course", new Course(courseId));
            return "course_students.html";
        }

        Course courseToAddStudent = this.courseService.getCourseById(courseId);
        studentToBeAdded.addCourseToStudent(courseToAddStudent);

        this.personService.updateStudent(studentToBeAdded);

        redirectAttributes.addFlashAttribute("success", "student added successfully");
        return "redirect:/admin/course/students?courseId=" + courseId;
    }

    @GetMapping("/course/student/remove")
    public String removeStudentFromCourse(
        @RequestParam(name = "studentId") Integer studentId,
        @RequestParam(name = "courseId") Integer courseId,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        Person student = this.personService.getStudentById(studentId);
        String errorMsg = PersonUtils.validateStudentBeforeRemovingFromCourse(student, courseId);

        if (errorMsg != null) {
            model.addAttribute("errorMsg", errorMsg);
            return "course_students.html";
        }

        Course courseToRemove = this.courseService.getCourseById(courseId);
        student.removeCourseFromStudent(courseToRemove);

        this.personService.updateStudent(student);

        String message = "Student removed from course successfully";
        redirectAttributes.addFlashAttribute("success", message);

        return "redirect:/admin/course/students?courseId=" + courseId;
    }
}
