package com.example.authentication;

public class HelperClass {

    String firstName;
    String lastName;
    String email;
    String contactNo;
    String parentContactNo;
    
    String dpJoinYear;
    Boolean requestAccepted;

    public HelperClass(String firstName, String lastName, String email, String contactNo, String parentContactNo, String dpJoinYear, Boolean requestAccepted) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.contactNo = contactNo;
        this.parentContactNo = parentContactNo;
        this.dpJoinYear = dpJoinYear;
        this.requestAccepted = requestAccepted;
    }

    public HelperClass() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getParentContactNo() {
        return parentContactNo;
    }

    public void setParentContactNo(String parentContactNo) {
        this.parentContactNo = parentContactNo;
    }

    public String getdpJoinYear() {
        return dpJoinYear;
    }

    public void setdpJoinYear(String dpJoinYear) {
        this.dpJoinYear = dpJoinYear;
    }

    
    public Boolean getRequest() {
        return requestAccepted;
    }

    public void setRequest(Boolean requestAccepted) {
        this.requestAccepted = requestAccepted;
    }


}
