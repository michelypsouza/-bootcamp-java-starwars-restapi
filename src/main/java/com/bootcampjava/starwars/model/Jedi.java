package com.bootcampjava.starwars.model;

public class Jedi {

    private Integer id;
    private String name;
    private Integer strength;
    private Integer version;

    public Jedi() {
    }

    public Jedi(Integer id, String name, Integer strength, Integer version) {
        this.id = id;
        this.name = name;
        this.strength = strength;
        this.version = version;
    }

    public Jedi(String name, Integer strength) {
        this.name = name;
        this.strength = strength;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStrength() {
        return strength;
    }

    public void setStrength(Integer strength) {
        this.strength = strength;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
