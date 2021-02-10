package io.forus.me.android.data.entity.records.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Record {

    @SerializedName("id")
    private Long id;

    @SerializedName("value")
    private String value;

    @SerializedName("order")
    private Long order;

    @SerializedName("key")
    private String key;

    @SerializedName("record_category_id")
    private Long recordCategoryId;

    @SerializedName("valid")
    private Boolean valid;

    @SerializedName("validations")
    private List<Validation> validations;

    @SerializedName("name")
    private String name;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public List<Validation> getValidations() {
        return validations;
    }

    public void setValidations(List<Validation> validations) {
        this.validations = validations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Record() {
    }

    public Record(Long id, String value, Long order, String key, Long recordCategoryId, Boolean valid, List<Validation> validations, String name) {
        this.id = id;
        this.value = value;
        this.order = order;
        this.key = key;
        this.recordCategoryId = recordCategoryId;
        this.valid = valid;
        this.validations = validations;
        this.name = name;
    }
}
