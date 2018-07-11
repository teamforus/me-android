package io.forus.me.android.data.entity.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity(indexes = {

})
public class RecordCategory {

    private Long id;

    private String name;

    private Long order;


    @Generated(hash = 1974307936)
    public RecordCategory(Long id, String name, Long order) {
        this.id = id;
        this.name = name;
        this.order = order;
    }

    @Generated(hash = 925350098)
    public RecordCategory() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOrder() {
        return this.order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }


    
}
