package com.gmat.quiz.generator.controller;

import com.gmat.quiz.generator.model.Quiz;
import com.gmat.quiz.generator.service.QuizService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author kapil.dev
 */
@Controller
public class QuizController {

    private static final Logger logger = LoggerFactory.getLogger(QuizController.class);

    @Autowired
    private QuizService quizService;

    @GetMapping("/")
    public String home(){
        return "upload";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   Model model) {
        boolean isInValid = false;
        String message = "";
        if(!file.getOriginalFilename().contains(".csv")){
            isInValid = true;
            message = "Please upload only CSV file!";
            logger.debug("Please upload only CSV file! File {} Not Accepted!", file.getOriginalFilename());
        } else if(file.isEmpty()){
            isInValid = true;
            message = "CSV file is empty, Please upload CSV file with Data!";
            logger.debug("CSV file is empty, Please upload CSV file with Data! File with size {} Not Accepted!", file.getSize());
        } else if( file.getSize() < 100){
            isInValid = true;
            message = "Please upload CSV file with Data!";
            logger.debug("Please upload CSV file with Data! File with size {} Not Accepted!", file.getSize());
        }

        if(isInValid){
            model.addAttribute("message", message);
            return "index";
        }

        try {
            Set<Quiz> quizSet= quizService.readCSVAndGetQuizRawData(file);
            Map<String, List<Quiz>> genQuiz =  quizService.processQuizSetAndGetGeneratedQuiz(quizSet);
            model.addAttribute("data", genQuiz);
            model.addAttribute("message", genQuiz.size()+" set of quizzes can be generated from uploaded csv file!");
        } catch (IOException e) {
            logger.error("Exception Thrown {}",e.getMessage());
        }
        return "quiztable";
    }
}