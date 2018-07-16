package io.forus.me.android.data.entity.common;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiError {

    @SerializedName("message")
    private String message;

    @SerializedName("errors")
    private Errors errors;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }

    public class Errors {

        @SerializedName("name")
        private List<String> name;

        @SerializedName("key")
        private List<String> key;

        @SerializedName("value")
        private List<String> value;

        public List<String> getName() {
            return name;
        }

        public void setName(List<String> name) {
            this.name = name;
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
    }
}
