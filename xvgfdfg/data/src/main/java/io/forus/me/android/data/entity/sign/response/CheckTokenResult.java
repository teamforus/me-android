package io.forus.me.android.data.entity.sign.response;

import com.google.gson.annotations.SerializedName;

public class CheckTokenResult {

    public enum State {
        active, invalid, pending
    }

    @SerializedName("message")
    private State state;

    public CheckTokenResult() { }

    public CheckTokenResult(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
