package com.example.alexander.afgangsprojekt_ucn.Models;

import java.util.Calendar;
import java.util.Date;

public class Steps {
    private Date startDate;
    private Date endDate;
    private int floorsAscended;
    private int floorsDescended;
    private int steps;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getFloorsAscended() {
        return floorsAscended;
    }

    public void setFloorsAscended(int floorsAscended) {
        this.floorsAscended = floorsAscended;
    }

    public int getFloorsDescended() {
        return floorsDescended;
    }

    public void setFloorsDescended(int floorsDescended) {
        this.floorsDescended = floorsDescended;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }
}
