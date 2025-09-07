package com.softworkshub.timetableschool.controller;

import com.softworkshub.timetableschool.dto.ScheduleRequest;
import com.softworkshub.timetableschool.dto.ScheduleResponse;
import com.softworkshub.timetableschool.model.Timetable;
import com.softworkshub.timetableschool.service.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/schedule")
public class TimetableController {

    private final ScheduleService scheduleService;

    public TimetableController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/generate")
    public ResponseEntity<ScheduleResponse> generate(@RequestBody ScheduleRequest req) {
        Timetable solved = scheduleService.generateSchedule(req);

        List<ScheduleResponse.Assignment> assignments = solved.getLessonList().stream()
                .filter(l -> l.getTimeslot() != null && !l.getTimeslot().isLunch())
                .map(l -> new ScheduleResponse.Assignment(
                        l.getClassName(),
                        l.getRoom().getName(),
                        l.getSubject().getName(),
                        l.getTeacher() == null ? "" : l.getTeacher().getName(),
                        l.getTimeslot().getStartTime() + "-" + l.getTimeslot().getEndTime()
                )).toList();

        return ResponseEntity.ok(new ScheduleResponse(assignments));
    }
}
