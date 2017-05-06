package com.github.codeloop.braillo.models;

/**
 * Created by dilpreet on 6/5/17.
 */

public class Chat {
    String message;
    String user;
    long time;

    public String getMessage() {
        return message;
    }

    public Chat(String message, String user, long time) {
        this.message = message;
        this.user = user;
        this.time = time;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
