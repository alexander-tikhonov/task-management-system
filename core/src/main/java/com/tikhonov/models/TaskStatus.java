package com.tikhonov.models;

import java.util.List;

public enum TaskStatus {
    NEW, CLOSED, ANALYSIS, DEVELOPMENT;

    static {
        NEW.prev = List.of(CLOSED);
        NEW.next = List.of(ANALYSIS, DEVELOPMENT);
    }

    private List<TaskStatus> prev;
    private List<TaskStatus> next;

    public List<TaskStatus> getPrev() {
        return prev;
    }

    public List<TaskStatus> getNext() {
        return next;
    }
}
