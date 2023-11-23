package io.forus.me.android.data.entity.vouchers.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Office {

    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("organization_id")
    @Expose
    private long organizationId;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("lon")
    @Expose
    private double lon;
    @SerializedName("lat")
    @Expose
    private double lat;
    @SerializedName("photo")
    @Expose
    private Logo photo;
    @SerializedName("organization")
    @Expose
    private Organization organization;
    @SerializedName("schedule")
    @Expose
    private List<Schedule> schedule = null;

    public Office() {
    }

    public Office(long id, long organizationId, String address, String phone, double lon, double lat, Logo photo, Organization organization, List<Schedule> schedule) {
        super();
        this.id = id;
        this.organizationId = organizationId;
        this.address = address;
        this.phone = phone;
        this.lon = lon;
        this.lat = lat;
        this.photo = photo;
        this.organization = organization;
        this.schedule = schedule;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(long organizationId) {
        this.organizationId = organizationId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public Logo getPhoto() {
        return photo;
    }

    public void setPhoto(Logo photo) {
        this.photo = photo;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public List<Schedule> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<Schedule> schedule) {
        this.schedule = schedule;
    }

}