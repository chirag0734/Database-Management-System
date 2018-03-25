package com.restapi.model;

public class Size {
    private int sizeId;
    private String sizeName;
    private int priceFactor;

    public Size(int sizeId, String sizeName, int priceFactor) {
        this.sizeId = sizeId;
        this.sizeName = sizeName;
        this.priceFactor = priceFactor;
    }

    public Size() {
    }

    public Size(String sizeName) {
        this.sizeName = sizeName;
    }

    public int getSizeId() {
        return sizeId;
    }

    public void setSizeId(int sizeId) {
        this.sizeId = sizeId;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public int getPriceFactor() {
        return priceFactor;
    }

    public void setPriceFactor(int priceFactor) {
        this.priceFactor = priceFactor;
    }

    @Override
    public String toString() {
        return "Size{" +
                "sizeId=" + sizeId +
                ", size='" + sizeName + '\'' +
                ", priceFactor=" + priceFactor +
                '}';
    }
}
