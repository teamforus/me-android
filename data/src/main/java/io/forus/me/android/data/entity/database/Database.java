package io.forus.me.android.data.entity.database;

public interface Database {

    void refresh();

    boolean exists();

    boolean isOpen();

    boolean open(String pin);

    boolean checkPin(String pin);

    boolean close();

    boolean delete();
}
