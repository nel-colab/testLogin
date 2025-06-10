package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class PhoneEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long number;
    private int citycode;
    private String contrycode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private UserEntity user;

    // Constructors
    public PhoneEntity() {
        // Default constructor for JPA
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public int getCitycode() {
        return citycode;
    }

    public void setCitycode(int citycode) {
        this.citycode = citycode;
    }

    public String getContrycode() {
        return contrycode;
    }

    public void setContrycode(String contrycode) {
        this.contrycode = contrycode;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", number=" + number +
                ", citycode=" + citycode +
                ", contrycode='" + contrycode + '\'' +
                ", user=" + (user != null ? user.getId() : null) +
                '}';
    }
}