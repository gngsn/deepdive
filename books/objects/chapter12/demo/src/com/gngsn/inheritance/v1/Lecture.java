package com.gngsn.inheritance.v1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Lecture class.
 *
 * 수강생들의 이수 여부 (합격/낙제) 통계 결과 출력.
 * ex. `Pass:3 Fail:2`
 */
public class Lecture {
    private int pass;
    private String title;
    private List<Integer> scores = new ArrayList<>();

    public Lecture(String title, int pass, List<Integer> scores) {
        this.title = title;
        this.pass = pass;
        this.scores = scores;
    }

    public double average() {
        return scores.stream()
                .mapToInt(Integer::intValue)
                .average().orElse(0);
    }

    public List<Integer> getScores() {
        return Collections.unmodifiableList(scores);
    }

    public String evaluate() {
        return String.format("Pass:%d Fail:%d", passCount(), failCount());
    }

    private long passCount() {
        return scores.stream().filter(score -> score >= pass).count();
    }

    private long failCount() {
        return scores.size() - passCount();
    }

    @Override
    public String toString() {
        return "Lecture{" +
                "pass=" + pass +
                ", title='" + title + '\'' +
                ", scores=" + scores +
                '}';
    }
}