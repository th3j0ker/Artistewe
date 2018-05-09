package com.typhon.studios.artistewe_prototype_3.listViewExample;

public class person {
    String firstname;
    String lastname;
    String speciallity;

    public person(String firstname, String lastname, String speciallity) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.speciallity = speciallity;
    }

    public person() {
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

    public String getSpeciallity() {
        return speciallity;
    }

    public void setSpeciallity(String speciallity) {
        this.speciallity = speciallity;
    }
}
