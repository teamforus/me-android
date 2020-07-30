package io.forus.me.android.data.entity.records.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by maestrovs on 29.04.2020.
 */
public class ValidatorOrganization {

    @SerializedName("id")
    private Long id;

    @SerializedName("name")
    private String name;


    public ValidatorOrganization(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
