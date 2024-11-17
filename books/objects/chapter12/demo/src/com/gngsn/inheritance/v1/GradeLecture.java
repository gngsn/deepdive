package com.gngsn.inheritance.v1;

import java.util.List;

import static java.util.stream.Collectors.joining;

public class GradeLecture extends Lecture {
    private List<Grade> grades;

    public GradeLecture(String name, int pass, List<Grade> grades, List<Integer> scores) {
        super(name, pass, scores);
        this.grades = grades;
    }

    // ======= 부모 클래스 오버라이딩 메서드 =======

    /**
     * 학생들의 '이수 여부 + 등급별 통계' 함께 반환
     * - super를 이용해 Lecture 클래스의 evaluate 메서드를 먼저 실행
     *
     * 메서드 오버라이딩
     * - 자식 클래스 안에 상속받은 메서드와 동일한 시그니처의 메서드를 재정의하여,
     *   부모 클래스의 구현을 새로운 구현으로 대체
     */
    @Override
    public String evaluate() {
        return super.evaluate() + ", " + gradesStatistics();
    }

    private String gradesStatistics() {
        return grades.stream()
                .map(grade -> format(grade))
                .collect(joining(" "));
    }

    private String format(Grade grade) {
        return String.format("%s:%d", grade.getName(), gradeCount(grade));
    }

    private long gradeCount(Grade grade) {
        return getScores().stream()
                .filter(grade::include)
                .count();
    }

    // ======= 부모 클래스에는 없던 새로운 메서드를 추가 =======

    /**
     * 부모 클래스인 Lecture와 이름은 같지만 시그니처는 다름
     *
     * 오버로딩: 메서드와 이름은 동일하지만 시그니처는 다른 메서드를 자식 클래스에 추가하는 것을 메서드
     */
    public double average(String gradeName) {
        return grades.stream()
                .filter(each -> each.isName(gradeName))
                .findFirst()
                .map(this::gradeAverage)
                .orElse(0d);
    }

    private double gradeAverage(Grade grade) {
        return getScores().stream()
                .filter(grade::include)
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0);
    }

    @Override
    public String toString() {
        return "GradeLecture{" +
                "grades=" + this.grades +
                '}';
    }
}
