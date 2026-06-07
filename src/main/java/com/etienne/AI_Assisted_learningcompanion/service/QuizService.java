package com.etienne.AI_Assisted_learningcompanion.service;

import com.etienne.AI_Assisted_learningcompanion.model.Question;
import com.etienne.AI_Assisted_learningcompanion.model.QuizResult;
import com.etienne.AI_Assisted_learningcompanion.model.User;
import com.etienne.AI_Assisted_learningcompanion.repository.QuestionRepository;
import com.etienne.AI_Assisted_learningcompanion.repository.QuizResultRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class QuizService {

    private final QuestionRepository questionRepository;
    private final QuizResultRepository quizResultRepository;
    private final AchievementService achievementService;
    private final AIService aiService;

    public QuizService(
            QuestionRepository questionRepository,
            QuizResultRepository quizResultRepository,
            AchievementService achievementService,
            AIService aiService
    ) {
        this.questionRepository = questionRepository;
        this.quizResultRepository = quizResultRepository;
        this.achievementService = achievementService;
        this.aiService = aiService;
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public List<Question> getQuestionsByLesson(Long lessonId) {
        return questionRepository.findByLessonId(lessonId);
    }

    public Question saveQuestion(Question question) {
        return questionRepository.save(question);
    }

    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }

    public Question getQuestionById(Long id) {
        return questionRepository.findById(id).orElse(null);
    }

    public void generateAndSaveQuizForLesson(Long lessonId) {

        List<Question> existingQuestions =
                questionRepository.findByLessonId(lessonId);

        if (existingQuestions.size() >= 5) {
            return;
        }

        List<Question> generatedQuestions =
                aiService.generateQuizFromLesson(lessonId);

        int remainingSlots =
                5 - existingQuestions.size();

        int count = 0;

        for (Question question : generatedQuestions) {

            if (count >= remainingSlots) {
                break;
            }

            questionRepository.save(question);
            count++;
        }
    }

    public QuizResult submitQuiz(
            User student,
            Map<String, String> answers
    ) {
        List<Question> questions = questionRepository.findAll();

        return calculateAndSaveResult(
                student,
                questions,
                answers,
                "Quiz Master",
                "Awarded for scoring 80% or higher in a quiz."
        );
    }

    public QuizResult submitLessonQuiz(
            User student,
            Long lessonId,
            Map<String, String> answers
    ) {
        List<Question> questions =
                questionRepository.findByLessonId(lessonId);

        return calculateAndSaveResult(
                student,
                questions,
                answers,
                "Lesson Master",
                "Awarded for scoring 80% or higher in a lesson quiz."
        );
    }

    private QuizResult calculateAndSaveResult(
            User student,
            List<Question> questions,
            Map<String, String> answers,
            String badgeName,
            String badgeDescription
    ) {
        int correct = 0;

        for (Question question : questions) {
            String userAnswer =
                    answers.get("question_" + question.getId());

            if (userAnswer != null &&
                    userAnswer.equalsIgnoreCase(question.getCorrectAnswer())) {
                correct++;
            }
        }

        int total = questions.size();

        double percentage =
                total == 0 ? 0 : (correct * 100.0) / total;

        boolean passed = percentage >= 80;

        QuizResult result = new QuizResult();

        result.setStudent(student);
        result.setTotalQuestions(total);
        result.setCorrectAnswers(correct);
        result.setPercentage(percentage);
        result.setPassed(passed);

        if (passed) {
            achievementService.awardBadge(
                    student,
                    badgeName,
                    badgeDescription
            );
        }

        return quizResultRepository.save(result);
    }

    public List<QuizResult> getStudentResults(User student) {
        return quizResultRepository.findByStudent(student);
    }

    @Transactional
    public void regenerateQuiz(Long lessonId) {

        questionRepository.deleteByLessonId(lessonId);

        generateAndSaveQuizForLesson(lessonId);
    }
}