package com.ca.interfacingodoo;

import java.util.Date;
import java.util.Map;

public class SalesOrder {
    private Integer id;
    private String name;
    private int partner_id;
    private String partner_name;
    private String date_order;
    private int warehouse_id;
    private String warehouse_name;
    private String client_order_ref;
    private double amount_total;

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

    public int getPartner_id() {
        return partner_id;
    }

    public void setPartner_id(int partner_id) {
        this.partner_id = partner_id;
    }

    public String getPartner_name() {
        return partner_name;
    }

    public void setPartner_name(String partner_name) {
        this.partner_name = partner_name;
    }

    public String getDate_order() {
        return date_order;
    }

    public void setDate_order(String date_order) {
        this.date_order = date_order;
    }

    public int getWarehouse_id() {
        return warehouse_id;
    }

    public void setWarehouse_id(int warehouse_id) {
        this.warehouse_id = warehouse_id;
    }

    public String getWarehouse_name() {
        return warehouse_name;
    }

    public void setWarehouse_name(String warehouse_name) {
        this.warehouse_name = warehouse_name;
    }

    public String getClient_order_ref() {
        return client_order_ref;
    }

    public void setClient_order_ref(String client_order_ref) {
        this.client_order_ref = client_order_ref;
    }

    public double getAmount_total() {
        return amount_total;
    }

    public void setAmount_total(double amount_total) {
        this.amount_total = amount_total;
    }

    public void setdata(Map<String,Object> classObj){
        setId((Integer) classObj.get("id"));
        setName(OdooUtility.getString(classObj,"name"));
        M2OField partner_id = OdooUtility.getMany2One(classObj,"partner_id");
        setPartner_id(partner_id.id);
        setPartner_name(partner_id.value);
        M2OField warehouse_id = OdooUtility.getMany2One(classObj,"warehouse_id");
        setWarehouse_id(warehouse_id.id);
        setWarehouse_name(warehouse_id.value);
        setClient_order_ref(OdooUtility.getString(classObj,"client_order_ref"));
        setAmount_total(OdooUtility.getDouble(classObj,"amount_total"));
        setDate_order(OdooUtility.getString(classObj,"date_order"));

    }
}
