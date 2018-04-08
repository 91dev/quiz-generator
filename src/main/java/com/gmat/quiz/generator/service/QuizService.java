package com.gmat.quiz.generator.service;

import com.gmat.quiz.generator.model.Quiz;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author kapil.dev
 */
public interface QuizService {
    Set<Quiz> readCSVAndGetQuizRawData(MultipartFile file) throws IOException;
    Map<String, List<Quiz>> processQuizSetAndGetGeneratedQuiz(Set<Quiz> quizSet);
}
