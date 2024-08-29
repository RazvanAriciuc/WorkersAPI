package com.example.restful.worker;

import java.util.Date;

public class Worker {

    private int worker_id;
    private int age;
    private String firstName;
    private String lastName;
    private String workPlace;
    private String city;
    private String country;
    private Date birthDate;

    public Worker(int worker_id, int age, String firstName, String lastName, String workPlace, String city, String country, Date birthDate) {
        this.worker_id = worker_id;
        this.age = age;
        this.firstName = firstName;
        this.lastName = lastName;
        this.workPlace = workPlace;
        this.city = city;
        this.country = country;
        this.birthDate = birthDate;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setId(int worker_id) {
        this.worker_id = worker_id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public int getAge() {
        return age;
    }

    public int getId() {
        return worker_id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getWorkPlace() {
        return workPlace;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    @Override
    public String toString() {
        return "Worker{" +
                "age=" + age +
                ", id=" + worker_id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", workPlace='" + workPlace + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
