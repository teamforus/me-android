package io.forus.me.android.presentation.models;

public class ValidationResult {

    private boolean value;

    private String message;


    public ValidationResult(boolean value, String message) {
        this.value = value;
        this.message = message;
    }

    public boolean getValid() {
        return value;
    }

    public void setValid(boolean value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
