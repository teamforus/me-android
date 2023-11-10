package io.forus.me.android.data.entity.database;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

@Entity(indexes = {

})
public class Token  {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    private String token;

    public Token(String token) {
        this.token = token;
    }

    @Generated(hash = 1923768890)
    public Token(Long id, @NotNull String token) {
        this.id = id;
        this.token = token;
    }

    @Generated(hash = 79808889)
    public Token() {
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
}
