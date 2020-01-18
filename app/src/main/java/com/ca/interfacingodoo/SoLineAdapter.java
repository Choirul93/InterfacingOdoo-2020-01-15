package com.ca.interfacingodoo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SoLineAdapter extends RecyclerView.Adapter<SoLineAdapter.MyViewHOlder> {
    private Context context;
    private List<SalesOrderLine> salesOrderLineList;



    public SoLineAdapter(Context context, List<SalesOrderLine> salesOrderLineList) {
        this.context = context;
        this.salesOrderLineList = salesOrderLineList;
    }

    @NonNull
    @Override
    public MyViewHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sales_order_line,parent,false);
        return new MyViewHOlder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHOlder holder, int position) {
        SalesOrderLine salesOrderLine = salesOrderLineList.get(position);
        holder.tvName.setText(salesOrderLine.getProduct_name());
        holder.tvQty.setText(String.valueOf(salesOrderLine.getProduct_uom_qty()));
        holder.tvUom.setText(salesOrderLine.getProduct_uom_name());
        double price_total = salesOrderLine.getProduct_uom_qty()*salesOrderLine.getPrice_unit();
        holder.tvPrice.setText(String.valueOf(price_total));
    }

    @Override
    public int getItemCount() {
        if(salesOrderLineList != null)return salesOrderLineList.size();
        else return 0;
    }

    public class MyViewHOlder extends  RecyclerView.ViewHolder{

        TextView tvName, tvQty, tvUom, tvPrice;

        public MyViewHOlder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvQty = itemView.findViewById(R.id.tvQty);
            tvUom = itemView.findViewById(R.id.tvUom);
            tvPrice = itemView.findViewById(R.id.tvPrice);

        }
    }
}
