package io.forus.me.android.data.entity.records.request;

import com.google.gson.annotations.SerializedName;

public class CreateRecord {

    @SerializedName("key")
    private String key;

    @SerializedName("record_category_id")
    private Long recordCategoryId;

    @SerializedName("value")
    private String value;

    @SerializedName("order")
    private Long order;


    public CreateRecord() {}

    public CreateRecord(String key, Long recordCategoryId, String value, Long order) {
        this.key = key;
        this.recordCategoryId = recordCategoryId;
        this.value = value;
        this.order = order;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getRecordCategoryId() {
        return recordCategoryId;
    }

    public void setRecordCategoryId(Long recordCategoryId) {
        this.recordCategoryId = recordCategoryId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }
}
