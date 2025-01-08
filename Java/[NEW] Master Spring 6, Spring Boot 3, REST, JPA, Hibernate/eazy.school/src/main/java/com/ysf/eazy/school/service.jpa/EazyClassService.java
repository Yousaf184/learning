package com.ysf.eazy.school.service.jpa;

import com.ysf.eazy.school.dao.jpa.EazyClassRepository;
import com.ysf.eazy.school.model.jpa.EazyClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void saveNewClass(EazyClass newClass) {
        this.eazyClassRepository.save(newClass);
    }
}
