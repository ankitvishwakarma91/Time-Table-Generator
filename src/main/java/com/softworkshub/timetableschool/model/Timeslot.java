package com.softworkshub.timetableschool.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Timeslot {
    private String day; // We only need Monday (for 1 day)
    private String startTime;
    private String endTime;
    private boolean lunch;
}
