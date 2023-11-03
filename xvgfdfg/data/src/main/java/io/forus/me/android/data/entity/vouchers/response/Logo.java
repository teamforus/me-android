package io.forus.me.android.data.entity.vouchers.response;

import com.google.gson.annotations.SerializedName;

public class Logo {

    public class Sizes{

        @SerializedName("thumbnail")
        private String thumbnail;

        @SerializedName("large")
        private String large;

        public Sizes() { }

        public Sizes(String thumbnail, String large) {
            this.thumbnail = thumbnail;
            this.large = large;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getLarge() {
            return large;
        }

        public void setLarge(String large) {
            this.large = large;
        }
    }

    @SerializedName("uid")
    private String uid;

    @SerializedName("original_name")
    private String originalName;

    @SerializedName("type")
    private String type;

    @SerializedName("ext")
    private String ext;

    @SerializedName("sizes")
    private Sizes sizes;

    public Logo() { }

    public Logo(String uid, String originalName, String type, String ext, Sizes sizes) {
        this.uid = uid;
        this.originalName = originalName;
        this.type = type;
        this.ext = ext;
        this.sizes = sizes;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public Sizes getSizes() {
        return sizes;
    }

    public void setSizes(Sizes sizes) {
        this.sizes = sizes;
    }
}
