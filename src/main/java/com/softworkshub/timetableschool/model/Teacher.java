package com.softworkshub.timetableschool.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Teacher {
    private String name;
    private String subject;
    // 3 hrs as per input


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
