package com.ca.interfacingodoo;

import android.content.Intent;

import java.util.Map;

public class Partner {

    private Integer id;
    private String name;
    private String street;
    private int country_id;
    private String country_name;
    private String phone;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getCountry_id() {
        return country_id;
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setData(Map<String,Object> classObj){
        setId((Integer) classObj.get("id"));
        setName(OdooUtility.getString(classObj,"name"));
        setStreet(OdooUtility.getString(classObj,"street"));

        M2OField country_id = OdooUtility.getMany2One(classObj,"country_id");
        setCountry_id(country_id.id);
        setCountry_name(country_id.value);

        setPhone(OdooUtility.getString(classObj,"phone"));

    }
}
