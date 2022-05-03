package com.rrmchathura.cofee_order_app.Adapters.UserAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rrmchathura.cofee_order_app.Coffee_Details_Activity;
import com.rrmchathura.cofee_order_app.Filters.CoffeeFilter;
import com.rrmchathura.cofee_order_app.Filters.HomeCoffeeFilter;
import com.rrmchathura.cofee_order_app.Model.CoffeeModel;
import com.rrmchathura.cofee_order_app.R;
import com.rrmchathura.cofee_order_app.User_Coffee_Details_Activity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserCoffeeAdapter extends RecyclerView.Adapter<UserCoffeeAdapter.CoffeeViewHolder> implements Filterable{

    Context context;
    public ArrayList<CoffeeModel> coffeeList,filterList;
    public HomeCoffeeFilter filter;

    public UserCoffeeAdapter(Context context, ArrayList<CoffeeModel> coffeeList) {
        this.context = context;
        this.coffeeList = coffeeList;
        this.filterList = coffeeList;
    }

    @NonNull
    @Override
    public CoffeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.raw_user_menu_item_, parent, false);
        return new CoffeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoffeeViewHolder holder, int position) {

        CoffeeModel coffeeModel = coffeeList.get(position);

        String coffeeName = coffeeModel.getCoffee_name();
        String coffeeImage = coffeeModel.getCoffee_image();
        String coffeePrice = coffeeModel.getPrice();
        String coffeeId = coffeeModel.getCoffeeId();
        String isCustomizeAvailable = coffeeModel.getIsCustomizeCusAvailable();

        holder.coffee_name.setText(coffeeName);

        try {
            Picasso.get().load(coffeeImage).placeholder(R.drawable.spinner).into(holder.coffee_image);

        } catch (Exception e) {
            holder.coffee_image.setImageResource(R.drawable.spinner);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, User_Coffee_Details_Activity.class);
                intent.putExtra("coffeeName",coffeeName);
                intent.putExtra("coffeePrice",coffeePrice);
                intent.putExtra("coffeeImage",coffeeImage);
                intent.putExtra("coffeeId",coffeeId);
                intent.putExtra("isCustomizeAvailable",isCustomizeAvailable);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return coffeeList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new HomeCoffeeFilter(this, filterList);
        }
        return filter;
    }

    public class CoffeeViewHolder extends RecyclerView.ViewHolder {

        TextView price,coffee_name;
        ImageView coffee_image;

        public CoffeeViewHolder(@NonNull View itemView) {
            super(itemView);

            price = itemView.findViewById(R.id.price);
            coffee_image = itemView.findViewById(R.id.coffee_image);
            coffee_name = itemView.findViewById(R.id.coffee_name);
        }
    }


}
