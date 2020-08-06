package io.forus.me.android.data.entity.records.request;

import com.google.gson.annotations.SerializedName;

public class ValidateRecord {

    @SerializedName("organization_id")
    private Long organization_id;

    public ValidateRecord(Long organization_id) {
        this.organization_id = organization_id;
    }

    public Long getOrganization_id() {
        return organization_id;
    }

    public void setOrganization_id(Long organization_id) {
        this.organization_id = organization_id;
    }
}
