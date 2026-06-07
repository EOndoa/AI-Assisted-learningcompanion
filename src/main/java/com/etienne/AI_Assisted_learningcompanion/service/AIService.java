package com.etienne.AI_Assisted_learningcompanion.service;

import com.etienne.AI_Assisted_learningcompanion.model.Lesson;
import com.etienne.AI_Assisted_learningcompanion.model.Question;
import com.etienne.AI_Assisted_learningcompanion.repository.LessonRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AIService {

    private final LessonRepository lessonRepository;
    private final OpenRouterService openRouterService;

    public AIService(
            LessonRepository lessonRepository,
            OpenRouterService openRouterService
    ) {
        this.lessonRepository = lessonRepository;
        this.openRouterService = openRouterService;
    }

    public String generateResponse(String question) {

        if (question == null || question.trim().isEmpty()) {
            return "Please type a valid academic question.";
        }

        Lesson bestLesson = findBestLesson(question);

        String context = "";

        if (bestLesson != null && bestLesson.getContent() != null) {
            context = bestLesson.getContent();

            if (context.length() > 4000) {
                context = context.substring(0, 4000);
            }
        }

        String prompt = """
                You are an AI Learning Assistant for students.

                Answer the student's question clearly, academically and simply.

                Student question:
                %s

                Lesson context:
                %s

                Instructions:
                - Use the lesson context if relevant.
                - Explain like a teacher.
                - Give one short example.
                - End with a learning tip.
                """.formatted(question, context);

        String aiAnswer = openRouterService.askOpenRouter(prompt);

        if (aiAnswer == null || aiAnswer.isBlank()) {
            return buildFallbackAnswer(question);
        }

        return aiAnswer;
    }

    public String summarizeLesson(Long lessonId) {

        Lesson lesson = lessonRepository.findById(lessonId).orElse(null);

        if (lesson == null) {
            return "Lesson not found.";
        }

        String content = lesson.getContent();

        if (content == null || content.isBlank()) {
            return "No lesson content available to summarize.";
        }

        if (content.length() > 5000) {
            content = content.substring(0, 5000);
        }

        String prompt = """
                Summarize this lesson for a student.

                Lesson title:
                %s

                Lesson content:
                %s

                Format:
                1. Main idea
                2. Key points
                3. Simple example
                4. Study tip
                """.formatted(lesson.getTitle(), content);

        return openRouterService.askOpenRouter(prompt);
    }

    public List<Question> generateQuizFromLesson(Long lessonId) {

        Lesson lesson = lessonRepository.findById(lessonId).orElse(null);

        List<Question> questions = new ArrayList<>();

        if (lesson == null || lesson.getContent() == null || lesson.getContent().isBlank()) {
            return questions;
        }

        String content = lesson.getContent();

        if (content.length() > 5000) {
            content = content.substring(0, 5000);
        }

        String prompt = """
                Generate exactly 5 multiple-choice quiz questions from this lesson.

                Lesson title:
                %s

                Lesson content:
                %s

                Return the quiz in this exact format:

                QUESTION: ...
                A: ...
                B: ...
                C: ...
                D: ...
                ANSWER: A
                DIFFICULTY: Easy
                EXPLANATION: ...

                Use Easy for questions 1-2, Medium for questions 3-4, Hard for question 5.

                Important:
                - Do not use Markdown tables.
                - Do not add numbering before QUESTION.
                - Keep answer options short and clear.
                """.formatted(lesson.getTitle(), content);

        String aiResponse = openRouterService.askOpenRouter(prompt);

        return parseQuestions(aiResponse, lesson);
    }

    private List<Question> parseQuestions(String aiResponse, Lesson lesson) {

        List<Question> questions = new ArrayList<>();

        if (aiResponse == null || aiResponse.isBlank()) {
            return questions;
        }

        String[] blocks = aiResponse.split("QUESTION:");

        for (String block : blocks) {

            if (questions.size() == 5) {
                break;
            }

            if (!block.contains("A:") || !block.contains("B:") ||
                    !block.contains("C:") || !block.contains("D:") ||
                    !block.contains("ANSWER:")) {
                continue;
            }

            Question question = new Question();

            question.setLesson(lesson);
            question.setQuestionText(limit(clean(extract(block, "", "A:")), 1800));
            question.setOptionA(limit(clean(extract(block, "A:", "B:")), 900));
            question.setOptionB(limit(clean(extract(block, "B:", "C:")), 900));
            question.setOptionC(limit(clean(extract(block, "C:", "D:")), 900));
            question.setOptionD(limit(clean(extract(block, "D:", "ANSWER:")), 900));

            String answer = clean(extract(block, "ANSWER:", "DIFFICULTY:"));

            if (answer.length() > 1) {
                answer = answer.substring(0, 1);
            }

            question.setCorrectAnswer(answer.isBlank() ? "A" : answer);

            String difficulty = extract(block, "DIFFICULTY:", "EXPLANATION:");
            question.setDifficultyLevel(clean(difficulty).isBlank() ? "Medium" : clean(difficulty));

            String explanation = extract(block, "EXPLANATION:", null);
            question.setExplanation(limit(clean(explanation), 1800));

            questions.add(question);
        }

        return questions;
    }

    private String extract(String text, String start, String end) {

        int startIndex = start.isEmpty() ? 0 : text.indexOf(start);

        if (startIndex < 0) {
            return "";
        }

        startIndex += start.length();

        int endIndex = end == null ? text.length() : text.indexOf(end, startIndex);

        if (endIndex < 0) {
            endIndex = text.length();
        }

        return text.substring(startIndex, endIndex).trim();
    }

    private String clean(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("*", "")
                .replace("-", "")
                .replace("#", "")
                .trim();
    }

    private String limit(String value, int maxLength) {

        if (value == null) {
            return "";
        }

        if (value.length() <= maxLength) {
            return value;
        }

        return value.substring(0, maxLength) + "...";
    }

    private Lesson findBestLesson(String question) {

        List<String> keywords = extractKeywords(question);
        List<Lesson> lessons = lessonRepository.findAll();

        Lesson bestLesson = null;
        int bestScore = 0;

        for (Lesson lesson : lessons) {

            String title = lesson.getTitle() == null ? "" : lesson.getTitle().toLowerCase();
            String content = lesson.getContent() == null ? "" : lesson.getContent().toLowerCase();

            int score = 0;

            for (String keyword : keywords) {
                if (title.contains(keyword)) {
                    score += 3;
                }

                if (content.contains(keyword)) {
                    score += 1;
                }
            }

            if (score > bestScore) {
                bestScore = score;
                bestLesson = lesson;
            }
        }

        return bestLesson;
    }

    private List<String> extractKeywords(String question) {

        String cleaned = question.toLowerCase()
                .replace("what is", "")
                .replace("what are", "")
                .replace("explain", "")
                .replace("define", "")
                .replace("tell me about", "")
                .replace("how does", "")
                .replace("how do", "")
                .replace("why is", "")
                .replace("?", "")
                .replace(".", "")
                .replace(",", "")
                .trim();

        String[] words = cleaned.split("\\s+");

        List<String> keywords = new ArrayList<>();

        for (String word : words) {
            if (word.length() > 3) {
                keywords.add(word);
            }
        }

        return keywords;
    }

    private String buildFallbackAnswer(String question) {
        return "I could not generate an AI response now. Please check your OpenRouter API configuration or try again.";
    }
}