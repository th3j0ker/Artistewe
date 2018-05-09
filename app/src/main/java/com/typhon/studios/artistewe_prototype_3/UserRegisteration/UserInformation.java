package com.typhon.studios.artistewe_prototype_3.UserRegisteration;

public class UserInformation {
    String firstname;
    String lastname;
    String email;
    String age;
    String profession;
    String country;
    String userid;
    String profileimage;

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public UserInformation(String firstname, String lastname, String email, String age, String profession, String country, String userid, String profileimage) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.age = age;
        this.profession = profession;
        this.country = country;
        this.userid = userid;
        this.profileimage = profileimage;
    }

    public UserInformation(String firstname, String lastname, String email, String age, String profession, String country, String userid) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.age = age;
        this.profession = profession;
        this.country = country;
        this.userid = userid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public UserInformation() {
    }

    public UserInformation(String firstname, String lastname, String email, String age, String profession, String country) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.age = age;
        this.profession = profession;
        this.country = country;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
