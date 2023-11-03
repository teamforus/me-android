package io.forus.me.android.data.entity.validators.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListAllValidationRequests {

    @SerializedName("data")
    private List<ValidationRequest> data;

    public ListAllValidationRequests() { }

    public ListAllValidationRequests(List<ValidationRequest> data) {
        this.data = data;
    }

    public List<ValidationRequest> getData() {
        return data;
    }

    public void setData(List<ValidationRequest> data) {
        this.data = data;
    }
}
