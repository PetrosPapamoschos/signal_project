package com.alerts;

public class PriorityAlertDecorator extends AlertDecorator {
    private final int priority;
    public PriorityAlertDecorator(Alert decorated, int priority) {
        super(decorated);
        this.priority = priority;
    }
    public int getPriority() { 
        return priority; }
}