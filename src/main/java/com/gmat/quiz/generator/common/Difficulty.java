package com.gmat.quiz.generator.common;

/**
 * @author kapil.dev
 */
public enum Difficulty {
    EASY("EASY"), MEDIUM("MEDIUM"), HARD("HARD");

    private String difficulty;

    Difficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public Difficulty next() {
        switch (this) {
            case EASY:
                return MEDIUM;
            case MEDIUM:
                return HARD;
            case HARD:
                return EASY;
            default:
                return EASY;
        }
    }
}
