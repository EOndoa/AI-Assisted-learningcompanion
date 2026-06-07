package com.etienne.AI_Assisted_learningcompanion.service;

import com.etienne.AI_Assisted_learningcompanion.model.Major;
import com.etienne.AI_Assisted_learningcompanion.repository.MajorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MajorService {

    private final MajorRepository majorRepository;

    public MajorService(MajorRepository majorRepository) {
        this.majorRepository = majorRepository;
    }

    public List<Major> getAllMajors() {
        return majorRepository.findAll();
    }

    public Major getMajorById(Long id) {
        return majorRepository.findById(id).orElse(null);
    }

    public Major saveMajor(Major major) {
        return majorRepository.save(major);
    }

    public void deleteMajor(Long id) {
        majorRepository.deleteById(id);
    }
}