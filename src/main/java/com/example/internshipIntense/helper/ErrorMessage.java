package com.example.internshipIntense.helper;

import io.swagger.v3.oas.annotations.media.Schema;

public class ErrorMessage {

    @Schema(description = "Error message", example = "Some error message")
    private String message;

    public ErrorMessage(String message) {
        this.message = message;
    }

    public ErrorMessage() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
