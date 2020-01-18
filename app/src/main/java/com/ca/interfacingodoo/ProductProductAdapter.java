package com.ca.interfacingodoo;

import android.content.Context;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.w3c.dom.Text;

import java.util.List;

public class ProductProductAdapter extends RecyclerView.Adapter<ProductProductAdapter.MyViewHolder> {

    private Context context;
    private List<ProductProduct> productProductList;
    ProductProductAdapterListener listener;

    public ProductProductAdapter(Context context, List<ProductProduct> productProductList, ProductProductAdapterListener listener) {
        this.context = context;
        this.productProductList = productProductList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ProductProduct productProduct = productProductList.get(position);
        holder.tVName.setText(productProduct.getName());
        holder.tvPrice.setText(String.valueOf(productProduct.getLst_price()));

        if(!productProduct.getImage_medium().isEmpty()){
            byte[] imageByteArray = Base64.decode(productProduct.getImage_medium(), Base64.DEFAULT);
            Glide.with(context)
                    .load(imageByteArray)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.ivProduct);

        }


    }

    @Override
    public int getItemCount() {
        if(productProductList != null ) return productProductList.size();
        else return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tVName, tvPrice, tvUom, tvQty;
        ImageView ivProduct;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tVName = itemView.findViewById(R.id.tvName);
            tvQty = itemView.findViewById(R.id.tvQty);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvUom = itemView.findViewById(R.id.tvUom);
            ivProduct = itemView.findViewById(R.id.ivProduct);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.click(productProductList.get(getAdapterPosition()));
                }
            });

        }
    }

    public interface ProductProductAdapterListener{
        void click(ProductProduct productProduct);
    }
}
