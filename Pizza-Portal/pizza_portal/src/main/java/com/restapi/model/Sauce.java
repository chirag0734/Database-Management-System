package com.restapi.model;

public class Sauce {
    private int sauceId;
    private String sauceName;

    public Sauce() {
    }

    public Sauce(String sauceName) {
        this.sauceName = sauceName;
    }

    public Sauce(int sauceId, String sauceName) {
        this.sauceId = sauceId;
        this.sauceName = sauceName;
    }

    public int getSauceId() {
        return sauceId;
    }

    public void setSauceId(int sauceId) {
        this.sauceId = sauceId;
    }

    public String getSauceName() {
        return sauceName;
    }

    public void setSauceName(String sauceName) {
        this.sauceName = sauceName;
    }

    @Override
    public String toString() {
        return "Sauce{" +
                "sauceId=" + sauceId +
                ", sauceName='" + sauceName + '\'' +
                '}';
    }
}
