package io.forus.me.android.data.entity.common;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiError extends BaseError{

    @SerializedName("errors")
    private Errors errors;

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

        @SerializedName("email")
        private List<String> email;

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

        public List<String> getEmail() {
            return email;
        }

        public void setEmail(List<String> email) {
            this.email = email;
        }
    }
}
