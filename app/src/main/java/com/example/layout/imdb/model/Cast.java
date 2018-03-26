package com.example.layout.imdb.model;

import java.io.Serializable;

/**
 * Created by Pri on 10/21/2017.
 */

public class Cast implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String character;
    private String profile_path;
    private String job;
    private String key;

    public Cast() {
    }

/*
This program will have getter and setter methods to the cast which implements serializable
 Serializable interface can be saved and restored by the serialization facilities
     */
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getProfilePath() {
        return profile_path;
    }

    public void setProfilePath(String profile_path) {
        this.profile_path = profile_path;
    }

}
