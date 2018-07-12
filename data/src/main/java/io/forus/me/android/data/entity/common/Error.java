package io.forus.me.android.data.entity.common;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Error {

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

        public List<String> getName() {
            return name;
        }

        public void setName(List<String> name) {
            this.name = name;
        }
    }
}
