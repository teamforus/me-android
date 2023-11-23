package io.forus.me.android.domain.models.records.errors;

import java.util.List;

public class NewRecordError {

    private String message;

    private List<String> key;

    private List<String> value;

    public NewRecordError(String message, List<String> key, List<String> value) {
        this.message = message;
        this.key = key;
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getKey() {
        return key;
    }

    public void setKey(List<String> key) {
        this.key = key;
    }

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "NewRecordError{" +
                "message='" + message + '\'' +
                ", key=" + key +
                ", value=" + value +
                '}';
    }
}
