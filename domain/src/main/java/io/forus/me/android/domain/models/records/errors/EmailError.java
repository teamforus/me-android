package io.forus.me.android.domain.models.records.errors;

import java.util.List;

public class EmailError {

    private String message;

    private List<String> email;

    public EmailError(String message, List<String> name) {
        this.message = message;
        this.email = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getEmail() {
        return email;
    }

    public String getEmailFormatted() {

        if(email == null) {
            return null;
        }else {
            if(email.size() == 0){
                return null;
            }else {

                String result = "";
                for (String str : email) {
                    result += str;
                    result += "\n";
                }
                return result;
            }
        }
    }

    public void setEmail(List<String> email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "NewRecordCategoryError{" +
                "message='" + message + '\'' +
                ", name=" + email +
                '}';
    }
}
