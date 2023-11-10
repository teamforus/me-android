package io.forus.me.android.data.entity.validators.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListAllValidators {

    @SerializedName("data")
    private List<Validator> data;

    public ListAllValidators() { }

    public ListAllValidators(List<Validator> data) {
        this.data = data;
    }

    public List<Validator> getData() {
        return data;
    }

    public void setData(List<Validator> data) {
        this.data = data;
    }
}
