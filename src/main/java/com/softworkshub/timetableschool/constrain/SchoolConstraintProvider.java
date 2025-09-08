package com.softworkshub.timetableschool.constrain;


import com.softworkshub.timetableschool.model.Lesson;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;

public class SchoolConstraintProvider implements ConstraintProvider {
    @Override
    public Constraint[] defineConstraints(ConstraintFactory factory) {
        return new Constraint[] {
                teacherConflict(factory),
                roomConflict(factory),
                classConflict(factory),
                unassignSubject(factory)
        };
    }

    private Constraint teacherConflict(ConstraintFactory factory) {
        return factory.forEachUniquePair(Lesson.class,
                        Joiners.equal(Lesson::getTimeslot),
                        Joiners.equal(l -> l.getTeacher() == null ? "" : l.getTeacher().getName()))
                .filter((a,b) -> a.getTeacher() != null && b.getTeacher() != null)
                .penalize("Teacher conflict", HardSoftScore.ONE_HARD);
    }

    private Constraint roomConflict(ConstraintFactory factory) {
        return factory.forEachUniquePair(Lesson.class,
                        Joiners.equal(Lesson::getTimeslot),
                        Joiners.equal(Lesson::getRoom))
                .penalize("Room conflict", HardSoftScore.ONE_HARD);
    }

    private Constraint classConflict(ConstraintFactory factory) {
        return factory.forEachUniquePair(Lesson.class,
                        Joiners.equal(Lesson::getTimeslot),
                        Joiners.equal(Lesson::getClassName))
                .penalize("Class conflict", HardSoftScore.ONE_HARD);
    }

    /**
     *  HARD CONSTRAINTS : Every Subject must assign
     *  */

    private Constraint unassignSubject(ConstraintFactory factory) {
        return factory.forEach(Lesson.class)
                .filter(lesson -> lesson.getTimeslot() == null || lesson.getRoom() == null)
                .penalize("Unassign Subject", HardSoftScore.ONE_HARD);
    }


}
