package io.forus.me.android.data.entity.common;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

/**
 * Parse errors for 422 status codes
 */
public class DetailsError extends BaseError{

    @SerializedName("errors")
    private JsonObject errors;

    public JsonObject getErrors() {
        return errors;
    }

    public void setErrors(JsonObject errors) {
        this.errors = errors;
    }
}
