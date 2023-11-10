package io.forus.me.android.data.entity.records.request;

import com.google.gson.annotations.SerializedName;

public class UpdateCategory {

    @SerializedName("_method")
    private final String _method = "PATCH";

    @SerializedName("name")
    private String name;

    @SerializedName("order")
    private Long order;

    public UpdateCategory() {
    }

    public UpdateCategory(String name, Long order) {
        this.name = name;
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }
}
