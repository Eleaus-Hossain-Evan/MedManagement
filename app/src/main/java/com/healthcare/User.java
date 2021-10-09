package com.healthcare;

import java.lang.reflect.Array;

public class User {
    private String email, firstname, lastname, uid,friends, phone;
    private boolean loggedIn;

    public User(String email, String firstname, String lastname, String uid, String phone, boolean loggedIn, String friends) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.uid = uid;
        this.phone = phone;
        this.loggedIn = loggedIn;
        this.friends = friends;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setuid(String uid) {
        this.uid = uid;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public void setFriends(String friends) {
        this.friends = friends;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getuid() {
        return uid;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public String getFriends() {
        return friends;
    }
}
