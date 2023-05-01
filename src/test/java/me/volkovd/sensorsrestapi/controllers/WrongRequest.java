package me.volkovd.sensorsrestapi.controllers;

public class WrongRequest {

    private final String body;
    private final String field;
    private final String error;

    public WrongRequest(String body, String field, String error) {
        this.body = body;
        this.field = field;
        this.error = error;
    }

    public String getBody() {
        return body;
    }

    public String getField() {
        return field;
    }

    public String getError() {
        return error;
    }

}
