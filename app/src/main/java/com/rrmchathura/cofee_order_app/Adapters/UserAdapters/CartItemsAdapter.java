package com.rrmchathura.cofee_order_app.Adapters.UserAdapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.makeramen.roundedimageview.RoundedImageView;
import com.rrmchathura.cofee_order_app.Model.CartItemsModel;
import com.rrmchathura.cofee_order_app.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.CartViewHolder> {

    Context context;
    ArrayList<CartItemsModel> cartItemsList;
    FirebaseAuth mAuth;
    FirebaseDatabase database;

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

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

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
            holder.cardBackground.setBackgroundResource(R.drawable.card_bg);

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
            holder.cardBackground.setBackgroundResource(R.drawable.card_bg);
        }

        try {
            Picasso.get().load(coffeeImage).placeholder(R.drawable.spinner).into(holder.CoffeeImage);

        } catch (Exception e) {
            holder.CoffeeImage.setImageResource(R.drawable.spinner);
        }

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadDeleteDialogBox(timestamp,uid);
            }
        });

    }

    private void LoadDeleteDialogBox(String timestamp, String uid) {

        Log.d("UIDDD",uid);


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete");
        builder.setMessage("Do you want to delete this item?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                DeleteItem(timestamp,dialogInterface,uid);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void DeleteItem(String timestamp, DialogInterface dialogInterface, String uid) {

        DatabaseReference databaseReference = database.getReference("Users");
        databaseReference.child(uid).child("CartItems").child(timestamp).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                dialogInterface.dismiss();
                Toast.makeText(context,"Item Deleted",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItemsList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        TextView coffeeName,itemPrice,sizeTv;
        LinearLayout customizeItemsLL,additionsLL;
        ImageView CoffeeImage,sizeIv,sugarTypeIv,additionsIv;
        RelativeLayout cardBackground;
        ImageButton deleteBtn;

        public CartViewHolder(@NonNull View itemView) {
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
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            additionsLL = itemView.findViewById(R.id.additionsLL);
        }
    }
}
