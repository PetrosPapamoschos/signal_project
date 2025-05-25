package com.alerts;

public abstract class AlertDecorator extends Alert {
    protected final Alert decorated;
    public AlertDecorator(Alert decorated) {
        super(decorated.getPatientId(), decorated.getCondition(), decorated.getTimestamp());
        this.decorated = decorated;
    }
    @Override public String getCondition() { return decorated.getCondition(); }
    @Override public String getPatientId() { return decorated.getPatientId(); }
    @Override public long getTimestamp() { return decorated.getTimestamp(); }
}