package com.softworkshub.timetableschool.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class ScheduleRequest {
    private List<String> listOfRoomNo;
    private String startingTime;      // HH:mm
    private int intervalMinutes;
    private int noOfPeriods;
    private int lunchAfterPeriod;
    private int lunchMinutes;
    private List<String> classes;
    private List<TeacherDto> teacherList;
    private Map<String, List<String>> classSubjects;

    //  Mapping: Class → Room (e.g., {"Class1":"R1", "Class2":"R2"})
    private Map<String, String> classRoomMap;
}
