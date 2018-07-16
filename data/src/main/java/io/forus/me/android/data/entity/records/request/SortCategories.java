package io.forus.me.android.data.entity.records.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SortCategories {

    @SerializedName("_method")
    private final String _method = "PATCH";

    @SerializedName("categories")
    private List<Long> categories;

    public SortCategories() {
    }

    public SortCategories(List<Long> categories) {
        this.categories = categories;
    }
}
