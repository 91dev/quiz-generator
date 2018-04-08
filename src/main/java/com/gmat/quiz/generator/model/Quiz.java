package com.gmat.quiz.generator.model;

import com.gmat.quiz.generator.common.Difficulty;
import com.gmat.quiz.generator.common.Tag;

/**
 * @author kapil.dev
 */
public class Quiz {
    public String questionId;
    public Tag tag;
    public Difficulty difficulty;

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public String toString() {
        return "{" +
                "questionId='" + questionId + '\'' +
                ", tag=" + tag +
                ", difficulty=" + difficulty +
                '}';
    }

    public Quiz(String questionId, Tag tag, Difficulty difficulty) {
        this.questionId = questionId;
        this.tag = tag;
        this.difficulty = difficulty;
    }

    public Quiz() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Quiz quiz = (Quiz) o;

        return questionId.equals(quiz.questionId);
    }

    @Override
    public int hashCode() {
        return questionId.hashCode();
    }
}
