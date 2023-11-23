package io.forus.me.android.domain.models.records.errors;

import java.util.List;

public class NewRecordCategoryError {

    private String message;

    private List<String> name;

    public NewRecordCategoryError(String message, List<String> name) {
        this.message = message;
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "NewRecordCategoryError{" +
                "message='" + message + '\'' +
                ", name=" + name +
                '}';
    }
}
