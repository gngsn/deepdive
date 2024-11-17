package com.gngsn.inheritance.v2;

import com.gngsn.inheritance.v1.GradeLecture;
import com.gngsn.inheritance.v1.Lecture;

/**
 *
 */
public class Professor {
    private String name;
    private Lecture lecture;

    public Professor(String name, Lecture lecture) {
        this.name = name;
        this.lecture = lecture;
    }
    public String compileStatistics() {
        return String.format("[%s] %s - Avg: %.1f", name,
                lecture.evaluate(), lecture.average());
    }

    public void downcasting() {
        this.lecture = (GradeLecture) this.lecture;
    }


}