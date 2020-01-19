package com.ca.interfacingodoo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductProduct {
    private Integer id;
    private String  name;
    private String image_medium;
    private int uom_id;
    private String uom_name;
    private double lst_price;

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

    public String getImage_medium() {
        return image_medium;
    }

    public void setImage_medium(String image_medium) {
        this.image_medium = image_medium;
    }

    public int getUom_id() {
        return uom_id;
    }

    public void setUom_id(int uom_id) {
        this.uom_id = uom_id;
    }

    public String getUom_name() {
        return uom_name;
    }

    public void setUom_name(String uom_name) {
        this.uom_name = uom_name;
    }

    public double getLst_price() {
        return lst_price;
    }

    public void setLst_price(double lst_price) {
        this.lst_price = lst_price;
    }

    public void setData(Map<String,Object> classObj){
        setId((Integer) classObj.get("id"));
        setName(OdooUtility.getString(classObj,"name"));
        setImage_medium(OdooUtility.getString(classObj,"image_medium"));
        setLst_price(OdooUtility.getDouble(classObj,"lst_price"));
        M2OField uom_id = OdooUtility.getMany2One(classObj,"uom_id");
        setUom_id(uom_id.id);
        setUom_name(uom_id.value);
    }

    public static long insert(ProductProduct productProduct, Context context){
        ContentValues contentValues = new ContentValues();
        contentValues.put("odoo_id",productProduct.getId());
        contentValues.put("name",productProduct.getName());
        contentValues.put("image_medium", productProduct.getImage_medium());
        contentValues.put("lst_price",productProduct.getLst_price());
        contentValues.put("uom_id", productProduct.getUom_id());
        contentValues.put("uom_name",productProduct.getUom_name());

        return new DbHelper(context).insert(DbHelper.TABLE_PRODUCT_PRODUCT,contentValues);
    }

    public static List<ProductProduct> readAll(Context context){
        List<ProductProduct> productProductList = new ArrayList<>();
        Cursor cursor = DbHelper.getInstance(context).readAll(DbHelper.TABLE_PRODUCT_PRODUCT);

        cursor.moveToFirst();

        for(int i = 0; i<cursor.getCount(); i++){
            cursor.moveToPosition(i);
            ProductProduct productProduct = new ProductProduct();

            productProduct.setId(cursor.getInt(cursor.getColumnIndex("odoo_id")));
            productProduct.setImage_medium(cursor.getString(cursor.getColumnIndex("image_medium")));
            productProduct.setName(cursor.getString(cursor.getColumnIndex("name")));
            productProduct.setUom_id(cursor.getInt(cursor.getColumnIndex("uom_id")));
            productProduct.setUom_name(cursor.getString(cursor.getColumnIndex("uom_name")));
            productProduct.setLst_price(cursor.getDouble(cursor.getColumnIndex("lst_price")));
            productProductList.add(productProduct);

        }

        return productProductList;
    }


}
