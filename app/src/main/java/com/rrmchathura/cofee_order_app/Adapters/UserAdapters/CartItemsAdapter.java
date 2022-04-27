package com.rrmchathura.cofee_order_app.Adapters.UserAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.rrmchathura.cofee_order_app.Model.CartItemsModel;
import com.rrmchathura.cofee_order_app.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.CartViewHolder> {

    Context context;
    ArrayList<CartItemsModel> cartItemsList;

    public CartItemsAdapter(Context context, ArrayList<CartItemsModel> cartItemsList) {
        this.context = context;
        this.cartItemsList = cartItemsList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_cart_item,parent,false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

        CartItemsModel cartItemsModel = cartItemsList.get(position);

        String coffeeName = cartItemsModel.getCoffeeName();
        String isCustomizeAvailable = cartItemsModel.getIsCustomizeAvailable();
        String finalPrice = cartItemsModel.getFinalPrice();
        String coffeeImage = cartItemsModel.getCoffeeImage();
        String quantity = cartItemsModel.getQuantity();

        holder.coffeeName.setText(coffeeName);
        holder.itemPrice.setText("Rs. "+finalPrice);
        holder.itemQuantityTv.setText("Quantity: "+quantity);

        if (isCustomizeAvailable.equals("true")){

            holder.customizeItemsLL.setVisibility(View.VISIBLE);
            holder.cardBackground.setBackgroundResource(R.drawable.card_bg_one);
        }else {
            holder.customizeItemsLL.setVisibility(View.GONE);
            holder.cardBackground.setBackgroundResource(R.drawable.card_bg);
        }

        try {
            Picasso.get().load(coffeeImage).placeholder(R.drawable.spinner).into(holder.CoffeeImage);

        } catch (Exception e) {
            holder.CoffeeImage.setImageResource(R.drawable.spinner);
        }

    }

    @Override
    public int getItemCount() {
        return cartItemsList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        TextView coffeeName,itemPrice,itemQuantityTv,sizeTv;
        LinearLayout customizeItemsLL;
        ImageView CoffeeImage,sizeIv,sugarTypeIv,additionsIv;
        RelativeLayout cardBackground;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            coffeeName = itemView.findViewById(R.id.coffeeName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemQuantityTv = itemView.findViewById(R.id.itemQuantityTv);
            customizeItemsLL = itemView.findViewById(R.id.customizeItemsLL);
            CoffeeImage = itemView.findViewById(R.id.CoffeeImage);
            sizeIv = itemView.findViewById(R.id.sizeIv);
            sugarTypeIv = itemView.findViewById(R.id.sugarTypeIv);
            additionsIv = itemView.findViewById(R.id.additionsIv);
            cardBackground = itemView.findViewById(R.id.cardBackground);
        }
    }
}
