package com.github.codeloop.braillo.models;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by dilpreet on 6/5/17.
 */
@Table(database = MyDb.class)
public class User extends BaseModel{



    @PrimaryKey(autoincrement = true)
    @Column
    int serial;

    @Column
    long otherPhone;

    @Column
    String msg;

    @Column
    boolean read;

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public long getOtherPhone() {
        return otherPhone;
    }

    public void setOtherPhone(long otherPhone) {
        this.otherPhone = otherPhone;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
