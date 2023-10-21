package com.socialcodia.social_shopia.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.socialcodia.social_shopia.R;
import com.socialcodia.social_shopia.model.ModelProduct;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterProduct extends RecyclerView.Adapter<AdapterProduct.ViewHolder> {

    List<ModelProduct> modelProductList;
    Context context;

    public AdapterProduct(List<ModelProduct> modelProductList, Context context) {
        this.modelProductList = modelProductList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_product,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelProduct model = modelProductList.get(position);

        String productDiscountNote = model.getProductDiscountNote();
        String tvProductTitle = model.getProductTitle();
        String productQuantity = model.getProductQuantity();
        String productPrice = model.getProductPrice();
        String tvProductDiscountPrice = model.getProductDiscount();
        String productImage = model.getProductImage();

        //Set data
        holder.tvProductDiscountNote.setText(productDiscountNote);
        holder.tvProductTitle.setText(tvProductTitle);
        holder.tvProductQuantity.setText(productQuantity);
        holder.tvProductPrice.setText(productPrice);
        holder.tvProductDiscountPrice.setText(tvProductDiscountPrice);

        try{
            Picasso.get().load(productImage).into(holder.productImageIcon);
        }
        catch (Exception e)
        {
            Picasso.get().load(R.drawable.store).into(holder.productImageIcon);
        }
    }



    @Override
    public int getItemCount() {
        return modelProductList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        private TextView tvProductDiscountNote,tvProductTitle,tvProductQuantity,tvProductPrice,tvProductDiscountPrice;
        private ImageView productImageIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvProductDiscountNote = itemView.findViewById(R.id.tvProductDiscountNote);
            tvProductTitle = itemView.findViewById(R.id.tvProductTitle);
            tvProductQuantity = itemView.findViewById(R.id.tvProductQuantity);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvProductDiscountPrice = itemView.findViewById(R.id.tvProductDiscountPrice);
            productImageIcon = itemView.findViewById(R.id.productImageIcon);


        }
    }

}
