package com.example.authentication;

import java.io.Serializable;

public class User implements Serializable {

    private String name, email, contactNo, parentContactNo, joinYear;

    public User(String name, String email, String contactNo, String parentContactNo, String joinYear) {
        this.name = name;
        this.email = email;
        this.contactNo = contactNo;
        this.parentContactNo = parentContactNo;
        this.joinYear = joinYear;
    }

    public User() {

    }

    public String getName() {
        return name;
    }


    public String getEmail() {
        return email;
    }

    public String getContactNo() {
        return contactNo;
    }

    public String getParentContactNo() {
        return parentContactNo;
    }

    public String getJoinYear() {
        return joinYear;
    }

}
