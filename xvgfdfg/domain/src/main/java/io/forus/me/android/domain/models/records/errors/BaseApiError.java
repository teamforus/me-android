package io.forus.me.android.domain.models.records.errors;


public class BaseApiError {

    private String message;


    public BaseApiError(String message) {
        this.message = message;

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public String toString() {
        return "BaseApiError{" +
                "message='" + message + '\'' +
                '}';
    }
}
