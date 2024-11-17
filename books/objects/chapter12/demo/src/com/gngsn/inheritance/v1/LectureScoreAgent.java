package com.gngsn.inheritance.v1;

import java.util.Arrays;

public class LectureScoreAgent {
    public static void main(String[] args) {
        printLecture(); // "Pass:3 Fail:2"
        printGradeLecture(); // "Pass:3 Fail:2, A:1 B:1 C:1 D:1 F:1"
    }

    private static void printLecture() {
        Lecture lecture = new Lecture("객체지향 프로그래밍",
                70,
                Arrays.asList(81, 95, 75, 50, 45));

        String evaluation = lecture.evaluate();
        System.out.println(evaluation);
    }

    private static void printGradeLecture() {
        Lecture lecture = new GradeLecture("객체지향 프로그래밍",
                70,
                Arrays.asList(new Grade("A",100, 95),
                        new Grade("B",94, 80),
                        new Grade("C",79, 70),
                        new Grade("D",69, 50),
                        new Grade("F",49, 0)),
                Arrays.asList(81, 95, 75, 50, 45));
        final String evaluate = lecture.evaluate();

        System.out.println(evaluate);
    }
}
