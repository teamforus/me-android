package io.forus.me.android.domain.models.common;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Provide map of errors for 422 status codes
 */
public class DetailsApiError {

    private String message;


    private HashMap<String, String> errors;

    public DetailsApiError(String message, JsonObject errorsJson) {
        this.message = message;

        errors = new HashMap<>();

        Set<Map.Entry<String, JsonElement>> entrySet = errorsJson.entrySet();
        for (Map.Entry<String, JsonElement> entry : entrySet) {
            String key = entry.getKey();
            String value = "";
            try {
                value = entry.getValue().getAsString();
            } catch (Exception e) {
                Object valObj = entry.getValue();
                if (valObj instanceof JsonArray) {
                    for (int i = 0; i < ((JsonArray) valObj).size(); i++) {
                        Object item = ((JsonArray) valObj).get(i);
                        try {
                            String str = item.toString();
                            value = value + str;
                            value = value + "\n";
                        } catch (Exception ignored) {
                        }
                    }
                } else {
                    value = entry.getValue().toString();
                }
            }
            errors.put(key, value);
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HashMap<String, String> getErrors() {
        return errors;
    }


    @Override
    public String toString() {
        return "DetailsApiError{" +
                "message='" + message + '\'' +
                ", errors=" + errors.toString() +
                '}';
    }

    public String getErrorString() {
        StringBuilder msg = new StringBuilder();
        for (String key : errors.keySet()) {
            String value = errors.get(key);
            String err = key + ": " + value + "\n";
            msg.append(err);

        }
        return msg.toString();
    }

    public String getErrorStringWithoutKey() {
        StringBuilder msg = new StringBuilder();
        for (String key : errors.keySet()) {
            String value = errors.get(key);
            String err = value + "\n";
            msg.append(err);

        }
        return msg.toString();
    }

    public String getErrorsString() {
        StringBuilder msg = new StringBuilder();
        for (String key : errors.keySet()) {
            String value = errors.get(key);
            String err = value + "\n";
            msg.append(err);

        }
        return msg.toString();
    }
}
