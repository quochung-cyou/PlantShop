package com.quochung.plantshop.model;

public class Plant {

    String name, desc, type, url, color;
    Integer price;

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }


    public Integer getPrice() {
        return price;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }


    public String getType() {
        return type;
    }

}
