package com.etienne.AI_Assisted_learningcompanion.controller;

import com.etienne.AI_Assisted_learningcompanion.model.Course;
import com.etienne.AI_Assisted_learningcompanion.model.Lesson;
import com.etienne.AI_Assisted_learningcompanion.model.Major;
import com.etienne.AI_Assisted_learningcompanion.model.Question;
import com.etienne.AI_Assisted_learningcompanion.model.User;
import com.etienne.AI_Assisted_learningcompanion.service.AIUsageService;
import com.etienne.AI_Assisted_learningcompanion.service.CourseService;
import com.etienne.AI_Assisted_learningcompanion.service.EnrollmentService;
import com.etienne.AI_Assisted_learningcompanion.service.LessonService;
import com.etienne.AI_Assisted_learningcompanion.service.MajorService;
import com.etienne.AI_Assisted_learningcompanion.service.QuizService;
import com.etienne.AI_Assisted_learningcompanion.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdminController {

    private final MajorService majorService;
    private final CourseService courseService;
    private final LessonService lessonService;
    private final QuizService quizService;
    private final UserService userService;
    private final EnrollmentService enrollmentService;
    private final AIUsageService aiUsageService;

    public AdminController(
            CourseService courseService,
            LessonService lessonService,
            QuizService quizService,
            UserService userService,
            EnrollmentService enrollmentService,
            AIUsageService aiUsageService,
            MajorService majorService
    ) {
        this.courseService = courseService;
        this.lessonService = lessonService;
        this.quizService = quizService;
        this.userService = userService;
        this.enrollmentService = enrollmentService;
        this.aiUsageService = aiUsageService;
        this.majorService = majorService;
    }

    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        return user != null && "ADMIN".equalsIgnoreCase(user.getRole());
    }

    @GetMapping("/admin")
    public String adminDashboard(HttpSession session, Model model) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        model.addAttribute("totalCourses", courseService.getAllCourses().size());
        model.addAttribute("totalLessons", lessonService.getAllLessons().size());
        model.addAttribute("totalQuestions", quizService.getAllQuestions().size());
        model.addAttribute("totalStudents", userService.getAllStudents().size());
        model.addAttribute("totalEnrollments", enrollmentService.getAllEnrollments().size());

        return "admin-dashboard";
    }

    @GetMapping("/admin/majors")
    public String manageMajors(Model model, HttpSession session) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        model.addAttribute("majors", majorService.getAllMajors());
        model.addAttribute("major", new Major());

        return "admin-majors";
    }

    @PostMapping("/admin/majors/save")
    public String saveMajor(@ModelAttribute Major major, HttpSession session) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        majorService.saveMajor(major);

        return "redirect:/admin/majors";
    }

    @GetMapping("/admin/majors/edit/{id}")
    public String editMajor(
            @PathVariable Long id,
            Model model,
            HttpSession session
    ) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        model.addAttribute("major", majorService.getMajorById(id));
        model.addAttribute("majors", majorService.getAllMajors());

        return "admin-majors";
    }

    @GetMapping("/admin/majors/delete/{id}")
    public String deleteMajor(
            @PathVariable Long id,
            HttpSession session
    ) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        majorService.deleteMajor(id);

        return "redirect:/admin/majors";
    }

    @GetMapping("/admin/courses")
    public String manageCourses(Model model, HttpSession session) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("course", new Course());
        model.addAttribute("majors", majorService.getAllMajors());

        return "admin-courses";
    }

    @PostMapping("/admin/courses/save")
    public String saveCourse(
            @ModelAttribute Course course,
            HttpSession session
    ) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        courseService.saveCourse(course);

        return "redirect:/admin/courses";
    }

    @GetMapping("/admin/courses/edit/{id}")
    public String editCourse(
            @PathVariable Long id,
            Model model,
            HttpSession session
    ) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        model.addAttribute("course", courseService.getCourseById(id));
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("majors", majorService.getAllMajors());

        return "admin-courses";
    }

    @GetMapping("/admin/courses/delete/{id}")
    public String deleteCourse(
            @PathVariable Long id,
            HttpSession session
    ) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        courseService.deleteCourse(id);

        return "redirect:/admin/courses";
    }

    @GetMapping("/admin/lessons")
    public String manageLessons(Model model, HttpSession session) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        List<Lesson> lessons = lessonService.getAllLessons();

        Map<Long, Long> quizCountMap = new HashMap<>();

        for (Lesson lesson : lessons) {
            quizCountMap.put(
                    lesson.getId(),
                    (long) quizService.getQuestionsByLesson(lesson.getId()).size()
            );
        }

        model.addAttribute("lessons", lessons);
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("lesson", new Lesson());
        model.addAttribute("quizCountMap", quizCountMap);

        return "admin-lessons";
    }

    @PostMapping("/admin/lessons/save")
    public String saveLesson(
            @ModelAttribute Lesson lesson,
            @RequestParam(value = "pdfFile", required = false) MultipartFile pdfFile,
            HttpSession session
    ) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        lessonService.saveLessonWithPdf(lesson, pdfFile);

        return "redirect:/admin/lessons";
    }

    @GetMapping("/admin/lessons/edit/{id}")
    public String editLesson(
            @PathVariable Long id,
            Model model,
            HttpSession session
    ) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        List<Lesson> lessons = lessonService.getAllLessons();

        Map<Long, Long> quizCountMap = new HashMap<>();

        for (Lesson lesson : lessons) {
            quizCountMap.put(
                    lesson.getId(),
                    (long) quizService.getQuestionsByLesson(lesson.getId()).size()
            );
        }

        model.addAttribute("lesson", lessonService.getLessonById(id));
        model.addAttribute("lessons", lessons);
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("quizCountMap", quizCountMap);

        return "admin-lessons";
    }

    @GetMapping("/admin/lessons/delete/{id}")
    public String deleteLesson(
            @PathVariable Long id,
            HttpSession session
    ) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        lessonService.deleteLesson(id);

        return "redirect:/admin/lessons";
    }

    @GetMapping("/admin/lessons/{lessonId}/generate-quiz")
    public String generateLessonQuiz(
            @PathVariable Long lessonId,
            HttpSession session
    ) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        quizService.generateAndSaveQuizForLesson(lessonId);

        return "redirect:/admin/lessons";
    }

    @GetMapping("/admin/lessons/{lessonId}/regenerate-quiz")
    public String regenerateLessonQuiz(
            @PathVariable Long lessonId,
            HttpSession session
    ) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        quizService.regenerateQuiz(lessonId);

        return "redirect:/admin/lessons";
    }

    @GetMapping("/admin/questions")
    public String manageQuestions(Model model, HttpSession session) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        model.addAttribute("questions", quizService.getAllQuestions());
        model.addAttribute("question", new Question());
        model.addAttribute("lessons", lessonService.getAllLessons());

        return "admin-questions";
    }

    @PostMapping("/admin/questions/save")
    public String saveQuestion(
            @ModelAttribute Question question,
            HttpSession session
    ) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        quizService.saveQuestion(question);

        return "redirect:/admin/questions";
    }

    @GetMapping("/admin/questions/edit/{id}")
    public String editQuestion(
            @PathVariable Long id,
            Model model,
            HttpSession session
    ) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        model.addAttribute("question", quizService.getQuestionById(id));
        model.addAttribute("questions", quizService.getAllQuestions());
        model.addAttribute("lessons", lessonService.getAllLessons());

        return "admin-questions";
    }

    @GetMapping("/admin/questions/delete/{id}")
    public String deleteQuestion(
            @PathVariable Long id,
            HttpSession session
    ) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        quizService.deleteQuestion(id);

        return "redirect:/admin/questions";
    }

    @GetMapping("/admin/students")
    public String manageStudents(Model model, HttpSession session) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        model.addAttribute("students", userService.getAllUsers());

        return "admin-students";
    }

    @GetMapping("/admin/students/verify/{id}")
    public String verifyStudent(
            @PathVariable Long id,
            HttpSession session
    ) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        userService.verifyUser(id);

        return "redirect:/admin/students";
    }

    @GetMapping("/admin/students/disable/{id}")
    public String disableStudent(
            @PathVariable Long id,
            HttpSession session
    ) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        userService.disableUser(id);

        return "redirect:/admin/students";
    }

    @GetMapping("/admin/students/make-admin/{id}")
    public String makeAdmin(
            @PathVariable Long id,
            HttpSession session
    ) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        userService.makeAdmin(id);

        return "redirect:/admin/students";
    }

    @GetMapping("/admin/students/make-student/{id}")
    public String makeStudent(
            @PathVariable Long id,
            HttpSession session
    ) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        userService.makeStudent(id);

        return "redirect:/admin/students";
    }

    @GetMapping("/admin/ai-usage")
    public String aiUsage(Model model, HttpSession session) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        model.addAttribute("aiUsages", aiUsageService.getAllUsage());

        return "admin-ai-usage";
    }

    @GetMapping("/admin/reports")
    public String reports(Model model, HttpSession session) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        model.addAttribute("totalCourses", courseService.getAllCourses().size());
        model.addAttribute("totalLessons", lessonService.getAllLessons().size());
        model.addAttribute("totalQuestions", quizService.getAllQuestions().size());
        model.addAttribute("totalStudents", userService.getAllStudents().size());
        model.addAttribute("totalEnrollments", enrollmentService.getAllEnrollments().size());
        model.addAttribute("totalAIQuestions", aiUsageService.getAllUsage().size());

        return "admin-reports";
    }
}