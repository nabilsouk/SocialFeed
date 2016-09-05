package com.internship.socialfeed;

/**
 * Created by Nader on 31-Aug-16.
 */
public class User {

    private String name;
    private String email;
    private String password;

    public User(String email,String password) {
        this.email = email;
        this.password = password;
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        if (name == null)
        return "Email: "+email+" Password: "+password;
        else
            return "Name: "+name+" Email: "+email+" Password: "+password;
    }
}
