package io.forus.me.android.data.entity.records.request;

import com.google.gson.annotations.SerializedName;

public class CreateCategory {

    @SerializedName("order")
    private Long order;

    @SerializedName("name")
    private String name;

    public CreateCategory() {
    }

    public CreateCategory(Long order, String name) {
        this.order = order;
        this.name = name;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
