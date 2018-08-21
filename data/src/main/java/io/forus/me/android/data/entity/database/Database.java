package io.forus.me.android.data.entity.database;

public interface Database {

    boolean exists();

    boolean isOpen();

    boolean open(String pin);

    boolean close();

    boolean delete();
}
