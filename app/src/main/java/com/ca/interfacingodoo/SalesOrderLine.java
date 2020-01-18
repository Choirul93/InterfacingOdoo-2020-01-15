package com.ca.interfacingodoo;

import java.util.Map;

public class SalesOrderLine {
    private Integer id;
    private int product_id;
    private String product_name;
    private int product_uom_id;
    private String product_uom_name;
    private double product_uom_qty;
    private double price_unit;
    private double price_subtotal;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getProduct_uom_id() {
        return product_uom_id;
    }

    public void setProduct_uom_id(int product_uom_id) {
        this.product_uom_id = product_uom_id;
    }

    public String getProduct_uom_name() {
        return product_uom_name;
    }

    public void setProduct_uom_name(String product_uom_name) {
        this.product_uom_name = product_uom_name;
    }

    public double getProduct_uom_qty() {
        return product_uom_qty;
    }

    public void setProduct_uom_qty(double product_uom_qty) {
        this.product_uom_qty = product_uom_qty;
    }

    public double getPrice_unit() {
        return price_unit;
    }

    public void setPrice_unit(double price_unit) {
        this.price_unit = price_unit;
    }

    public double getPrice_subtotal() {
        return price_subtotal;
    }

    public void setPrice_subtotal(double price_subtotal) {
        this.price_subtotal = price_subtotal;
    }

    public void setData(Map<String,Object> classObj){
        setId((Integer) classObj.get("id"));
        M2OField product_id = OdooUtility.getMany2One(classObj,"product_id");
        setProduct_id(product_id.id);
        setProduct_name(product_id.value);
        M2OField product_uom_id = OdooUtility.getMany2One(classObj,"product_uom_id");
        setProduct_uom_id(product_uom_id.id);
        setProduct_uom_name(product_uom_id.value);
        setProduct_uom_qty(OdooUtility.getDouble(classObj,"product_uom_qty"));
        setPrice_unit(OdooUtility.getDouble(classObj,"price_unit"));
        setPrice_subtotal(OdooUtility.getDouble(classObj,"price_subtotal"));





    }
}
