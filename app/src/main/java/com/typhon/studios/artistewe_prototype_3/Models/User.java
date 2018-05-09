package com.typhon.studios.artistewe_prototype_3.Models;

/**
 * Created by root on 2/12/17.
 */

public class User {
    private String email_address;
    private String first_name;
    private long phone_number;
    private String userid;

    public User(String email_address, String first_name, long phone_number, String userid) {
        this.email_address = email_address;
        this.first_name = first_name;
        this.phone_number = phone_number;
        this.userid = userid;
    }

    public User(){

    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public long getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(long phone_number) {
        this.phone_number = phone_number;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "User{" +
                "email_address='" + email_address + '\'' +
                ", first_name='" + first_name + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", userid='" + userid + '\'' +
                '}';
    }
}
