package com.softworkshub.timetableschool.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data @AllArgsConstructor
public class ScheduleResponse {
    private List<Assignment> assignments;

    @Data @AllArgsConstructor
    public static class Assignment {
        private String className;
        private String roomNo;
        private String subject;
        private String teacherName;
        private String slot;
    }
}
