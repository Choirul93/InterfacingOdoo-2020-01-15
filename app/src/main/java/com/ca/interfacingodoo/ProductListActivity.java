package com.ca.interfacingodoo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.timroes.axmlrpc.XMLRPCCallback;
import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.XMLRPCServerException;

public class ProductListActivity extends AppCompatActivity implements ProductProductAdapter.ProductProductAdapterListener {
    private OdooUtility odoo;
    private String uid;
    private String password;
    private String serverAddress;
    private String database;

    private long searchTaskId;

    RecyclerView recycleviewProductList;
    TextView tvTotalPrice, tvItem, tvQty;
    ProgressBar pbProduct;

    List<ProductProduct> productProductList = new ArrayList<>();

    ProductProductAdapter productProductAdapter;

    List<SalesOrderLine> salesOrderLineList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        uid = SharedData.getKey(this,"uid");
        password = SharedData.getKey(this,"password");
        serverAddress = SharedData.getKey(this,"serverAddress");
        database = SharedData.getKey(this,"database");
        odoo = new OdooUtility(serverAddress,"object");

        recycleviewProductList = findViewById(R.id.recycleviewProductList);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvItem = findViewById(R.id.tvItem);
        pbProduct = findViewById(R.id.pbProduct);
        tvQty = findViewById(R.id.tvQty);

        productProductAdapter = new ProductProductAdapter(this,productProductList,this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recycleviewProductList.setLayoutManager(layoutManager);
        recycleviewProductList.setItemAnimator(new DefaultItemAnimator());
        recycleviewProductList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recycleviewProductList.setAdapter(productProductAdapter);

        onSync();

    }

    private void addToChart(ProductProduct productProduct){
        if(salesOrderLineList.size()>0){
            int size = salesOrderLineList.size();
            boolean isAdd = true;
            for(int i =0; i<size; i++){
                SalesOrderLine salesOrderLine = salesOrderLineList.get(i);
                if(salesOrderLine.getProduct_id()== productProduct.getId()){
                    double qty = salesOrderLine.getProduct_uom_qty();
                    salesOrderLine.setProduct_uom_qty(qty+1);
                    salesOrderLineList.set(i,salesOrderLine);
                    isAdd =false;
                }
            }

            if(isAdd){
                SalesOrderLine salesOrderLine = new SalesOrderLine();
                salesOrderLine.setProduct_id(productProduct.getId());
                salesOrderLine.setProduct_uom_qty(1);
                salesOrderLine.setProduct_uom_id(productProduct.getUom_id());
                salesOrderLine.setPrice_unit(productProduct.getLst_price());
                salesOrderLine.setProduct_name(productProduct.getName());
                salesOrderLineList.add(salesOrderLine);

            }

        }
        else{
            SalesOrderLine salesOrderLine = new SalesOrderLine();
            salesOrderLine.setProduct_id(productProduct.getId());
            salesOrderLine.setProduct_uom_qty(1);
            salesOrderLine.setProduct_uom_id(productProduct.getUom_id());
            salesOrderLine.setPrice_unit(productProduct.getLst_price());
            salesOrderLine.setProduct_name(productProduct.getName());
            salesOrderLineList.add(salesOrderLine);
        }
        updateItem();
    }

    public void updateItem(){
        int size = salesOrderLineList.size();
        double total_price = 0;
        double qty = 0;
        for (int i = 0; i<size; i++ ){
            total_price += salesOrderLineList.get(i).getPrice_unit()*salesOrderLineList.get(i).getProduct_uom_qty();
            qty += salesOrderLineList.get(i).getProduct_uom_qty();
        }
        tvItem.setText(String.valueOf(size)+" items");

        tvTotalPrice.setText("Rp "+String.valueOf(total_price));
        tvQty.setText(String.valueOf(qty));
    }

    public void showDetail(){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = this.getLayoutInflater().inflate(R.layout.item_buttom_sheet, null);
        bottomSheetDialog.setContentView(view);

        TextView tvProduct = view.findViewById(R.id.tvProduct);
        Button btnSoform = view.findViewById(R.id.btnSoform);
        btnSoform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SalesOrderDetailActivity.class);
                Gson gson = new Gson();
                String soLine = gson.toJson(salesOrderLineList);
                intent.putExtra("soLine",soLine);
                startActivity(intent);
            }
        });

        int size = salesOrderLineList.size();
        String product = "";

        for (int i= 0; i<size; i++){
            product += "\n"+salesOrderLineList.get(i).getProduct_name()+"\n"+
                    salesOrderLineList.get(i).getPrice_unit()+"\t x"+ salesOrderLineList.get(i).getProduct_uom_qty()+
                    "  "+salesOrderLineList.get(i).getProduct_uom_qty()*salesOrderLineList.get(i).getPrice_unit();

        }

        tvProduct.setText(product);
        bottomSheetDialog.show();

    }

    public void onSync() {
        pbProduct.setVisibility(View.VISIBLE);

        List conditions = Arrays.asList(Arrays.asList());

        Map fields = new HashMap(){{
            put("fields", Arrays.asList(
                    "id",
                    "name",
                    "image_medium",
                    "uom_id",
                    "lst_price"
            ));
        }};

        searchTaskId = odoo.search_read(listener,
                database,
                uid,
                password,
                "product.product",
                conditions,
                fields);

    }

    XMLRPCCallback listener = new XMLRPCCallback() {
        @Override
        public void onResponse(long id, Object result) {
            Looper.prepare();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pbProduct.setVisibility(View.GONE);
                }
            });
            if(id == searchTaskId){
                Object[] classObjs = (Object[]) result;
                int length = classObjs.length;
                productProductList.clear();
                //odoo.MessageDialog(ProductListActivity.this,"Length ==> "+length);
                if(length>0){

                    for(int i=0; i<length; i++){
                        ProductProduct productProduct = new ProductProduct();
                        Map<String, Object> classObj = (Map<String, Object>) classObjs[i];
                        productProduct.setData(classObj);


                        Log.i("", "setData: id ===>"+(Integer)classObj.get("id"));
                        productProductList.add(productProduct);

                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            productProductAdapter.notifyDataSetChanged();
                        }
                    });

                }
                else{
                    odoo.MessageDialog(ProductListActivity.this,"Product empty");
                }
            }
            Looper.loop();
        }

        @Override
        public void onError(long id, XMLRPCException error) {
            Looper.prepare();
            odoo.MessageDialog(ProductListActivity.this,error.getMessage());
            Looper.loop();

        }

        @Override
        public void onServerError(long id, XMLRPCServerException error) {
            Looper.prepare();
            odoo.MessageDialog(ProductListActivity.this,error.getMessage());
            Looper.loop();

        }
    };

    @Override
    public void click(ProductProduct productProduct) {
        addToChart(productProduct);
    }


    public void onDetail(View view) {
        showDetail();
    }
}
