package com.etienne.AI_Assisted_learningcompanion.service;

import com.etienne.AI_Assisted_learningcompanion.model.Lesson;
import com.etienne.AI_Assisted_learningcompanion.repository.LessonRepository;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class LessonService {

    private final LessonRepository lessonRepository;

    public LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    public List<Lesson> getLessonsByCourse(Long courseId) {
        return lessonRepository.findByCourseId(courseId);
    }

    public List<Lesson> getLessonsByCourseOrdered(Long courseId) {
        return lessonRepository.findByCourseIdOrderByIdAsc(courseId);
    }

    public Lesson getLessonById(Long id) {
        return lessonRepository.findById(id).orElse(null);
    }

    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }

    public Lesson saveLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    public Lesson saveLessonWithPdf(Lesson lesson, MultipartFile pdfFile) {

        try {
            if (pdfFile != null && !pdfFile.isEmpty()) {

                PDDocument document = Loader.loadPDF(pdfFile.getBytes());

                PDFTextStripper stripper = new PDFTextStripper();

                String pdfContent = stripper.getText(document);

                document.close();

                lesson.setContent(pdfContent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lessonRepository.save(lesson);
    }

    public void deleteLesson(Long id) {
        lessonRepository.deleteById(id);
    }
}