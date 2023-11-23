package io.forus.me.android.data.entity.records.request;

import com.google.gson.annotations.SerializedName;

public class UpdateRecord {

    @SerializedName("_method")
    private final String _method = "PATCH";

    @SerializedName("record_category_id")
    private Long recordCategoryId;

    @SerializedName("order")
    private Long order;

    public UpdateRecord() {
    }

    public UpdateRecord(Long recordCategoryId, Long order) {
        this.recordCategoryId = recordCategoryId;
        this.order = order;
    }

    public Long getRecordCategoryId() {
        return recordCategoryId;
    }

    public void setRecordCategoryId(Long recordCategoryId) {
        this.recordCategoryId = recordCategoryId;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }
}
