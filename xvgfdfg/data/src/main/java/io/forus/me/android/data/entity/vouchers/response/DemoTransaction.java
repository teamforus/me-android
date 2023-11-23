package io.forus.me.android.data.entity.vouchers.response;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.Date;

public class DemoTransaction {


    @SerializedName("data")
    private Data data;


    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("id")
        private Long id;


        @SerializedName("token")
        private String token;

        @SerializedName("created_at")
        private Date createdAt;

        @SerializedName("updated_at")
        private Date updatedAt;

        @SerializedName("state")
        private String state;


        public Data() {
        }

        public Data(Long id, String token, Date createdAt, Date updatedAt, String state) {
            this.id = id;
            this.token = token;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.state = state;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public Date getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }

        public Date getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(Date updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

    }
}
