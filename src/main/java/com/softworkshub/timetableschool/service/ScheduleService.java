package com.softworkshub.timetableschool.service;

import com.softworkshub.timetableschool.constrain.SchoolConstraintProvider;
import com.softworkshub.timetableschool.dto.ScheduleRequest;
import com.softworkshub.timetableschool.model.*;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.termination.TerminationConfig;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    public Timetable generateSchedule(ScheduleRequest req) {
        // Build Rooms
        List<Room> rooms = req.getListOfRoomNo().stream()
                .map(Room::new).toList();

        // Build Teachers
        List<Teacher> teachers = req.getTeacherList().stream()
                .map(t -> new Teacher(
                        t.getTeacherName(),
                        t.getSubject() == null ? "" : t.getSubject().trim()
                ))
                .toList();

        // Debugging - log all teachers
        teachers.forEach(t ->
                System.out.println("Teacher mapped: " + t.getName() + " | Subject: " + t.getSubject())
        );

        // Build Subjects
        Set<String> allSubjects = req.getClassSubjects().values().stream()
                .flatMap(List::stream).collect(Collectors.toSet());

        List<Subject> subjects = allSubjects.stream()
                .map(s -> new Subject(s, s.equalsIgnoreCase("Play")))
                .toList();

        // Build Timeslots
        List<Timeslot> timeslots = buildTimeslots(req);

        // Build Lessons
        List<Lesson> lessons = new ArrayList<>();
        long id = 1L;
        for (String cls : req.getClasses()) {
            for (String subj : req.getClassSubjects().get(cls)) {
                Teacher teacher = teachers.stream()
                        .filter(t -> t.getSubject() != null && t.getSubject().equalsIgnoreCase(subj)) // ✅ null-safe
                        .findFirst()
                        .orElse(null);

                Subject subject = subjects.stream()
                        .filter(s -> s.getName().equalsIgnoreCase(subj))
                        .findFirst()
                        .orElse(null);

                if (subject == null) {
                    throw new IllegalStateException("Subject not found: " + subj);
                }

                lessons.add(new Lesson(
                        id++,
                        cls,
                        subject,
                        subject.isFiller() ? null : teacher,
                        null,
                        null
                ));
            }
        }

        Timetable problem = new Timetable();
        problem.setRoomList(rooms);
        problem.setTeacherList(teachers);
        problem.setSubjectList(subjects);
        problem.setTimeslotList(timeslots);
        problem.setLessonList(lessons);


        SolverFactory<Timetable> factory =
                SolverFactory.create(new SolverConfig()
                        .withSolutionClass(Timetable.class)
                        .withEntityClasses(Lesson.class)
                        .withConstraintProviderClass(SchoolConstraintProvider.class)
                        //.withTerminationConfig(new TerminationConfig()
                         //       .withSecondsSpentLimit(1000L)));
                );

        Solver<Timetable> solver = factory.buildSolver();
        return solver.solve(problem);
    }

    private List<Timeslot> buildTimeslots(ScheduleRequest req) {
        List<Timeslot> result = new ArrayList<>();
        LocalTime start = LocalTime.parse(req.getStartingTime());
        for (int i = 1; i <= req.getNoOfPeriods(); i++) {
            LocalTime end = start.plusMinutes(req.getIntervalMinutes());
            result.add(new Timeslot("P" + i, start.toString(), end.toString(), false));
            start = end;
            if (i == req.getLunchAfterPeriod()) {
                LocalTime lunchEnd = start.plusMinutes(req.getLunchMinutes());
                result.add(new Timeslot("LUNCH", start.toString(), lunchEnd.toString(), true));
                start = lunchEnd;
            }
        }
        return result;
    }
}
