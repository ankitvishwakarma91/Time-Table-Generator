package com.softworkshub.timetableschool.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@PlanningEntity
public class Lesson {

    @PlanningId
    private Long id;

    private String className;

    private Subject subject;

    private Teacher teacher; // <-- new field

    @PlanningVariable(valueRangeProviderRefs = {"roomRange"})
    private Room room;

    @PlanningVariable(valueRangeProviderRefs = {"timeslotRange"})
    private Timeslot timeslot;
}
