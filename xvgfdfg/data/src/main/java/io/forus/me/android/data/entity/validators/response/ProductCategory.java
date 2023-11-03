package io.forus.me.android.data.entity.validators.response;

import com.google.gson.annotations.SerializedName;

public class ProductCategory {

    @SerializedName("id")
    private Long id;

    @SerializedName("key")
    private String key;

    @SerializedName("name")
    private String name;

    public ProductCategory() { }

    public ProductCategory(Long id, String key, String name) {
        this.id = id;
        this.key = key;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
