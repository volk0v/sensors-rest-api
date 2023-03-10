package me.volkovd.sensorsrestapi.utils;

import java.util.List;

public class FieldsErrorsResponse {

    private List<FieldErrors> errors;
    private long timestamp;

    public FieldsErrorsResponse(List<FieldErrors> errors, long timestamp) {
        this.errors = errors;
        this.timestamp = timestamp;
    }

    public List<FieldErrors> getErrors() {
        return errors;
    }

    public void setErrors(List<FieldErrors> errors) {
        this.errors = errors;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
