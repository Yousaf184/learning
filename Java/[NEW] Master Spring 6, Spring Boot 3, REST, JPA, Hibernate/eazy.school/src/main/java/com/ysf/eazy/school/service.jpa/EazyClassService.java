package com.ysf.eazy.school.service.jpa;

import com.ysf.eazy.school.dao.jpa.EazyClassRepository;
import com.ysf.eazy.school.model.jpa.EazyClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EazyClassService {

    private final EazyClassRepository eazyClassRepository;

    @Autowired
    public EazyClassService(EazyClassRepository eazyClassRepository) {
        this.eazyClassRepository = eazyClassRepository;
    }

    public List<EazyClass> getAllClasses() {
        return this.eazyClassRepository.findAll();
    }

    public void saveClass(EazyClass eazyClass) {
        this.eazyClassRepository.save(eazyClass);
    }

    public void deleteClassById(Integer classId) {
        Optional<EazyClass> classToDeleteOpt = this.eazyClassRepository.findById(classId);
        classToDeleteOpt.ifPresent(EazyClass::removeAllStudents);
        this.eazyClassRepository.deleteById(classId);
    }

    public EazyClass getEazyClassById(Integer classId) {
        Optional<EazyClass> eazyClassOpt = this.eazyClassRepository.findById(classId);

        return eazyClassOpt.orElseThrow(() -> {
            String message = "Class with the given id " + classId + " not found";
            return new IllegalArgumentException(message);
        });
    }
}
