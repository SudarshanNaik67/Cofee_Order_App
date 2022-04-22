package com.rrmchathura.cofee_order_app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.rrmchathura.cofee_order_app.Model.CoffeeModel;
import com.rrmchathura.cofee_order_app.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CoffeeAdapter extends RecyclerView.Adapter<CoffeeAdapter.CoffeeViewHolder> implements Filterable {

    Context context;
    public ArrayList<CoffeeModel> coffeeList,filterList;
    public CoffeeFilter filter;

    public CoffeeAdapter(Context context, ArrayList<CoffeeModel> coffeeList) {
        this.context = context;
        this.coffeeList = coffeeList;
        this.filterList = coffeeList;
    }

    @NonNull
    @Override
    public CoffeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.raw_menu_item, parent, false);
        return new CoffeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoffeeViewHolder holder, int position) {

        CoffeeModel coffeeModel = coffeeList.get(position);

        String coffeeName = coffeeModel.getCoffee_name();
        String coffeeImage = coffeeModel.getCoffee_image();
        String coffeePrice = coffeeModel.getPrice();
        String isCustomizeAvailable = coffeeModel.getIsCustomizeCusAvailable();

        holder.coffeeName.setText(coffeeName);

        try {
            Picasso.get().load(coffeeImage).placeholder(R.drawable.spinner).into(holder.CoffeeImage);

        } catch (Exception e) {
            holder.CoffeeImage.setImageResource(R.drawable.spinner);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Coffee_Details_Activity.class);
                intent.putExtra("coffeeName",coffeeName);
                intent.putExtra("coffeePrice",coffeePrice);
                intent.putExtra("coffeeImage",coffeeImage);
                intent.putExtra("isCustomizeAvailable",isCustomizeAvailable);
                context.startActivity(intent);
            }
        });

        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Coffee_Details_Activity.class);
                intent.putExtra("coffeeName",coffeeName);
                intent.putExtra("coffeePrice",coffeePrice);
                intent.putExtra("coffeeImage",coffeeImage);
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
            filter = new CoffeeFilter(this, filterList);
        }
        return filter;
    }

    public class CoffeeViewHolder extends RecyclerView.ViewHolder {

        TextView coffeeName;
        ImageView CoffeeImage;
        ImageButton more;

        public CoffeeViewHolder(@NonNull View itemView) {
            super(itemView);

            coffeeName = itemView.findViewById(R.id.coffeeName);
            CoffeeImage = itemView.findViewById(R.id.CoffeeImage);
            more = itemView.findViewById(R.id.more);
        }
    }


}
