package io.forus.me.android.data.repository.datasource;

import io.forus.me.android.data.entity.database.DaoSession;

public interface LocalDataSource {

    void updateDao(DaoSession daoSession);

}
