package com.gmat.quiz.generator.service.impl;

import com.gmat.quiz.generator.common.Difficulty;
import com.gmat.quiz.generator.common.Tag;
import com.gmat.quiz.generator.model.Quiz;
import com.gmat.quiz.generator.service.QuizService;
import com.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @author kapil.dev
 */
@Service
public class QuizServiceImpl implements QuizService {

    private static final Logger logger = LoggerFactory.getLogger(QuizServiceImpl.class);

    public static final String QUIZ_NO = "QuizNo : ";

    @Override
    /**
     * @method : Reading quizzes from csv file
     */
    public Set<Quiz> readCSVAndGetQuizRawData(MultipartFile file) throws IOException {
        logger.info("Start reading csv file ****");
        CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()), '|');
        Set<Quiz> quizzes = new HashSet<>();
        List<String[]> records = reader.readAll();
        int indexQid = 0, indexDiff = 0, indexTag = 0, index = 0;
        Iterator<String[]> iterator = records.iterator();
        Quiz quiz = null;
        while (iterator.hasNext()) {
            String[] record = iterator.next();
            if (index++ == 0){
                for (int i = 0; i < record.length; i++){
                    if ("Question ID".equalsIgnoreCase(record[i].trim())){
                        indexQid = i;
                    } else if ("Tag".equalsIgnoreCase(record[i].trim())){
                        indexTag = i;
                    } else if ("Difficulty".equalsIgnoreCase(record[i].trim())){
                        indexDiff = i;
                    }
                }
            } else if (record != null && record.length > 2){
                quiz = new Quiz(record[indexQid].trim(), Tag.getEnumTagByStringTag(record[indexTag].trim()), Difficulty.valueOf(record[indexDiff].trim().toUpperCase()));
                quizzes.add(quiz);
            }
        }
        logger.debug("Total Number of Quiz With Unique QuestionId from File : {}",quizzes.size());
        logger.info("End reading csv file ****");
        return quizzes;
    }

    @Override
    /**
     * @method : processing quizzes and putting in map to calculate possible set of quizzes can be generated,
     *          and generating the quiz
     */
    public Map<String, List<Quiz>> processQuizSetAndGetGeneratedQuiz(Set<Quiz> quizSet) {
        logger.info("Processing Started for Quiz Mapping ####");
        Map<String, List<Quiz>> finalRes = new LinkedHashMap<>();
        Map<Difficulty,Map<Tag,Queue<String>>> diffMap =new LinkedHashMap<>();
        Map<Tag,Queue<String>> tagMap = null;
        Queue<String> queue = null;
        for (Quiz quiz : quizSet){
            if(diffMap.containsKey(quiz.getDifficulty())){
                if(diffMap.get(quiz.getDifficulty()).containsKey(quiz.getTag())){
                    diffMap.get(quiz.getDifficulty()).get(quiz.getTag()).add(quiz.getQuestionId());
                }else{
                    queue = new LinkedList<>();
                    queue.add(quiz.getQuestionId());
                    diffMap.get(quiz.getDifficulty()).put(quiz.getTag(), queue);
                }
            } else {
                tagMap = new LinkedHashMap<>();
                queue = new LinkedList<>();
                queue.add(quiz.getQuestionId());
                tagMap.put(quiz.getTag(),queue);
                diffMap.put(quiz.getDifficulty(),tagMap);
            }
        }
        logger.info("Processing Ended for Quiz Mapping ####");
        int bucket = getPosibleQuizCount(quizSet.size(), diffMap);
        if (bucket == 0){
            return finalRes;
        }

        finalRes = finalQuizList(bucket, diffMap);
        return  finalRes;
    }

    /**
     * @method : processing map data to calculate possible set of quizzes can be generated
     */
    private int getPosibleQuizCount(int size, Map<Difficulty, Map<Tag, Queue<String>>> diffMap) {
        logger.info("Calculating Start for count of Set of Quezzes can be generated ####");
        int posibleQuiz = 0, maxQuiz = size/10, minTagCount = Integer.MAX_VALUE, minDiffCount = Integer.MAX_VALUE;
        if (!(size < 10 || diffMap.size() < 3)){
            int t1c = 0, t2c = 0, t3c =0, t4c = 0, t5c =0, t6c =0, ec = 0, mc = 0, hc = 0;
            for (Difficulty difficulty : diffMap.keySet()){
                int tag1 = 0, tag2=0, tag3 =0, tag4 =0, tag5 =0, tag6=0;
                if (diffMap.get(difficulty).containsKey(Tag.Tag1)){
                    tag1 = diffMap.get(difficulty).get(Tag.Tag1).size();
                    t1c += tag1;
                }
                if (diffMap.get(difficulty).containsKey(Tag.Tag2)){
                    tag2 = diffMap.get(difficulty).get(Tag.Tag2).size();
                    t2c += tag2;
                }
                if (diffMap.get(difficulty).containsKey(Tag.Tag3)){
                    tag3 = diffMap.get(difficulty).get(Tag.Tag3).size();
                    t3c += tag3;
                }
                if (diffMap.get(difficulty).containsKey(Tag.Tag4)){
                    tag4 = diffMap.get(difficulty).get(Tag.Tag4).size();
                    t4c += tag4;
                }
                if (diffMap.get(difficulty).containsKey(Tag.Tag5)){
                    tag5 = diffMap.get(difficulty).get(Tag.Tag5).size();
                    t5c += tag5;
                }
                if (diffMap.get(difficulty).containsKey(Tag.Tag6)){
                    tag6 = diffMap.get(difficulty).get(Tag.Tag6).size();
                    t6c += tag6;
                }
                logger.debug("In Loop Difficulty {} :: Tag1 : {}, Tag2 : {}, Tag3 : {}, Tag4 : {}, Tag5 : {}, Tag6 : {}",difficulty,tag1,tag2,tag3,tag4,tag5,tag6);
                if (Difficulty.EASY.equals(difficulty)){
                    ec = tag1+tag2+tag3+tag4+tag5+tag6;
                } else if (Difficulty.MEDIUM.equals(difficulty)){
                    mc = tag1+tag2+tag3+tag4+tag5+tag6;
                }else if (Difficulty.HARD.equals(difficulty)){
                    hc = tag1+tag2+tag3+tag4+tag5+tag6;
                }
            }
            logger.debug("Tag Count : Tag1 : {}, Tag2 : {}, Tag3 : {}, Tag4 : {}, Tag5 : {}, Tag6 : {}",t1c,t2c,t3c,t4c,t5c,t6c);
            logger.debug("Difficulty Count : EASY : {}, MEDIUM : {}, HARD : {} ",ec,mc,hc);
            if(t1c < minTagCount){
                minTagCount = t1c;
            }
            if(t2c < minTagCount){
                minTagCount = t2c;
            }
            if(t3c < minTagCount){
                minTagCount = t3c;
            }
            if(t4c < minTagCount){
                minTagCount = t4c;
            }
            if(t5c < minTagCount){
                minTagCount = t5c;
            }
            if(t6c < minTagCount){
                minTagCount = t6c;
            }
            logger.debug("Min Tag Count : {} ",minTagCount);
            if (ec < minDiffCount){
                minDiffCount = ec;
            }
            if (mc < minDiffCount){
                minDiffCount = mc;
            }
            if (hc < minDiffCount){
                minDiffCount = hc;
            }
            logger.debug("Min Diff Count : {} ",minDiffCount);
            if (minTagCount < maxQuiz){
                maxQuiz = minTagCount;
            }
            if(minDiffCount/2 < maxQuiz){
                maxQuiz = minDiffCount/2;
            }
            posibleQuiz = maxQuiz;
            logger.debug("Calculated Possible Quiz can be generated with Count : {} ",posibleQuiz);
        }
        logger.info("Calculating End for count of Set of Quezzes can be generated ####");
        return posibleQuiz;
    }

    /**
     * @method : Generating the set of quizzes from map and count of quiz can be generated
     *
     */
    public Map<String, List<Quiz>> finalQuizList(int quizBucket, Map<Difficulty,Map<Tag,Queue<String>>> diffMap){
        logger.info("Generating Set of Quizzes Start*****");
        Map<String, List<Quiz>> finalRes = new LinkedHashMap<>();
        for (int i = 1; i <= quizBucket; i++){
            Difficulty curDiff = Difficulty.EASY;
            Tag curTag = Tag.Tag1;
            finalRes.put(QUIZ_NO+i, new ArrayList<Quiz>());
            while (finalRes.get(QUIZ_NO+i).size() < 6){
                if(diffMap.containsKey(curDiff) && diffMap.get(curDiff).containsKey(curTag) && !diffMap.get(curDiff).get(curTag).isEmpty()){
                    finalRes.get(QUIZ_NO+i).add(new Quiz(diffMap.get(curDiff).get(curTag).remove(), curTag, curDiff));
                    if (diffMap.get(curDiff).get(curTag).isEmpty()){
                        diffMap.remove(diffMap.get(curDiff).containsKey(curTag));
                    }
                    curDiff = curDiff.next();
                    curTag = curTag.next();
                } else {
                    curDiff = curDiff.next();
                    if(diffMap.containsKey(curDiff) && diffMap.get(curDiff).containsKey(curTag) && !diffMap.get(curDiff).get(curTag).isEmpty()){
                        finalRes.get(QUIZ_NO+i).add(new Quiz(diffMap.get(curDiff).get(curTag).remove(), curTag, curDiff));
                        if (diffMap.get(curDiff).get(curTag).isEmpty()){
                            diffMap.remove(diffMap.get(curDiff).containsKey(curTag));
                        }
                        curDiff = curDiff.next();
                        curTag = curTag.next();
                    } else {
                        curDiff = curDiff.next();
                        if(diffMap.containsKey(curDiff) && diffMap.get(curDiff).containsKey(curTag) && !diffMap.get(curDiff).get(curTag).isEmpty()){
                            finalRes.get(QUIZ_NO+i).add(new Quiz(diffMap.get(curDiff).get(curTag).remove(), curTag, curDiff));
                            if (diffMap.get(curDiff).get(curTag).isEmpty()){
                                diffMap.remove(diffMap.get(curDiff).containsKey(curTag));
                            }
                            curDiff = curDiff.next();
                            curTag = curTag.next();
                        }
                    }
                }
            }
        }
        int count = 1;
        for (Difficulty difficulty : diffMap.keySet()){
            Map<Tag, Queue<String>> tagMap = diffMap.get(difficulty);
            for (Tag tag : tagMap.keySet()){
                if (!tagMap.get(tag).isEmpty()){
                    Queue<String> qq = tagMap.get(tag);
                    for (String qid : qq){
                        while (finalRes.get(QUIZ_NO+count).size() < 10){
                            finalRes.get(QUIZ_NO+count).add(new Quiz(qid,tag,difficulty));
                            if (count == quizBucket){
                                count = 1;
                            } else {
                                count++;
                            }
                            break;
                        }
                    }
                }
            }
        }

        logger.debug("Final Formed Quiz {}", finalRes);

        logger.info("Generating Set of Quizzes End *****");
        return finalRes;
    }
}
