package com.gngsn.inheritance.v2;

import com.gngsn.inheritance.v1.Grade;
import com.gngsn.inheritance.v1.GradeLecture;
import com.gngsn.inheritance.v1.Lecture;

import java.util.Arrays;

public class LectureScoreAgent {
    public static void main(String[] args) {
        calculateLectureByDijkstra2();
//        calculateGradeLectureByDijkstra();
    }

    /**
     * 다익스트라 교수가 강의하는 알고리즘 과목의 성적 통계를 계산
     */
    private static void calculateLectureByDijkstra2() {
        Lecture lecture = new GradeLecture("알고리즘",
                70,
                Arrays.asList(new Grade("A",100, 95),
                        new Grade("B",94, 80),
                        new Grade("C",79, 70),
                        new Grade("D",69, 50),
                        new Grade("F",49, 0)),
                Arrays.asList(81, 95, 75, 50, 45));
        GradeLecture gradeLecture = (GradeLecture) lecture;
        System.out.println(gradeLecture);
    }

    /**
     * 다익스트라 교수가 강의하는 알고리즘 과목의 성적 통계를 계산
     */
    private static void calculateLectureByDijkstra() {
        Professor professor = new Professor("다익스트라",
                new Lecture("알고리즘",
                        70,
                        Arrays.asList(81, 95, 75, 50, 45)));
        String statistics = professor.compileStatistics();
        System.out.println(statistics); // "[다익스트라] Pass:3 Fail:2 - Avg: 69.2"
    }

    /**
     * 다익스트라 교수가 강의하는 알고리즘 과목의 성적 통계를 계산
     *   + Lecture 클래스 대신 자식 클래스인 GradeLecture 인스턴스 전달
     */
    private static void calculateGradeLectureByDijkstra() {
        Professor professor = new Professor("다익스트라",
                new GradeLecture("알고리즘",
                        70,
                        Arrays.asList(new Grade("A",100, 95),
                                new Grade("B",94, 80),
                                new Grade("C",79, 70),
                                new Grade("D",69, 50),
                                new Grade("F",49, 0)),
                        Arrays.asList(81, 95, 75, 50, 45)));
        String statistics = professor.compileStatistics();
        System.out.println(statistics); // "[다익스트라] Pass:3 Fail:2, A:1 B:1 C:1 D:1 F:1 - Avg: 69.2"
    }

}
