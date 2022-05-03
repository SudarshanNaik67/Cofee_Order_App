package com.rrmchathura.cofee_order_app.Adapters.UserAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rrmchathura.cofee_order_app.Model.CartItemsModel;
import com.rrmchathura.cofee_order_app.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShowOrderDetailsAdapter extends RecyclerView.Adapter<ShowOrderDetailsAdapter.ViewHolder> {

    Context context;
    ArrayList<CartItemsModel> cartItemsModelArrayList;

    public ShowOrderDetailsAdapter(Context context, ArrayList<CartItemsModel> cartItemsModelArrayList) {
        this.context = context;
        this.cartItemsModelArrayList = cartItemsModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_orders_expandable_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CartItemsModel cartItemsModel = cartItemsModelArrayList.get(position);


        String coffeeName = cartItemsModel.getCoffeeName();
        String isCustomizeAvailable = cartItemsModel.getIsCustomizeAvailable();
        String finalPrice = cartItemsModel.getFinalPrice();
        String coffeeImage = cartItemsModel.getCoffeeImage();
        String quantity = cartItemsModel.getQuantity();
        String timestamp = cartItemsModel.getTimeStamp();
        String uid = cartItemsModel.getUid();


        holder.coffeeName.setText(coffeeName);
        holder.itemPrice.setText("Rs. "+finalPrice+" ("+quantity+")");

        if (isCustomizeAvailable.equals("true")){

            String selectedSize = cartItemsModel.getSelectedSize();
            String selectedSugar = cartItemsModel.getSelectedSugar();
            String selectedAdditions = cartItemsModel.getSelectedAdditions();

            holder.customizeItemsLL.setVisibility(View.VISIBLE);

            if (selectedSize.equals("small")){

                holder.sizeTv.setText("("+"S"+")");
                holder.sizeIv.setImageResource(R.drawable.small_tint);
            }
            else if (selectedSize.equals("medium")){
                holder.sizeTv.setText("("+"M"+")");
                holder.sizeIv.setImageResource(R.drawable.medium_tint);
            }
            else if (selectedSize.equals("large")){
                holder.sizeTv.setText("("+"L"+")");
                holder.sizeIv.setImageResource(R.drawable.large_tint);
            }
            else {}

            if (selectedSugar.equals("noSugar")){
                holder.sugarTypeIv.setImageResource(R.drawable.no_sugar_tint);
            }
            else if (selectedSugar.equals("smallSugar")){
                holder.sugarTypeIv.setImageResource(R.drawable.one_sugar_tint);
            }
            else if (selectedSugar.equals("mediumSugar")){
                holder.sugarTypeIv.setImageResource(R.drawable.medium_sugar_tint);
            }
            else if (selectedSugar.equals("highSugar")){
                holder.sugarTypeIv.setImageResource(R.drawable.high_sugar_tint);
            }
            else {}

            if (selectedAdditions.equals("empty")){
                holder.additionsLL.setVisibility(View.GONE);
            }
            else {
                holder.additionsLL.setVisibility(View.VISIBLE);

                if (selectedAdditions.equals("cream")){
                    holder.additionsIv.setImageResource(R.drawable.addition_cream_tint);
                }
                else if (selectedAdditions.equals("sticker")){
                    holder.additionsIv.setImageResource(R.drawable.addition_ice_tint);
                }
                else {}
            }

        }else {
            holder.customizeItemsLL.setVisibility(View.GONE);
        }

        try {
            Picasso.get().load(coffeeImage).placeholder(R.drawable.spinner).into(holder.CoffeeImage);

        } catch (Exception e) {
            holder.CoffeeImage.setImageResource(R.drawable.spinner);
        }
    }

    @Override
    public int getItemCount() {
        return cartItemsModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView coffeeName,itemPrice,sizeTv;
        LinearLayout customizeItemsLL,additionsLL;
        ImageView CoffeeImage,sizeIv,sugarTypeIv,additionsIv;
        RelativeLayout cardBackground;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            coffeeName = itemView.findViewById(R.id.coffeeName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            sizeTv = itemView.findViewById(R.id.sizeTv);
            customizeItemsLL = itemView.findViewById(R.id.customizeItemsLL);
            CoffeeImage = itemView.findViewById(R.id.CoffeeImage);
            sizeIv = itemView.findViewById(R.id.sizeIv);
            sugarTypeIv = itemView.findViewById(R.id.sugarTypeIv);
            additionsIv = itemView.findViewById(R.id.additionsIv);
            cardBackground = itemView.findViewById(R.id.cardBackground);
            additionsLL = itemView.findViewById(R.id.additionsLL);
        }
    }
}
