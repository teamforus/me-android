package io.forus.me.android.data.entity.records.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SortRecords {

    @SerializedName("_method")
    private final String _method = "PATCH";

    @SerializedName("records")
    private List<Long> records;

    public SortRecords() {
    }

    public SortRecords(List<Long> records) {
        this.records = records;
    }

    public List<Long> getRecords() {
        return records;
    }

    public void setRecords(List<Long> records) {
        this.records = records;
    }
}
