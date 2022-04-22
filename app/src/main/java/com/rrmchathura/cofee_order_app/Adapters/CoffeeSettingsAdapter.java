package com.rrmchathura.cofee_order_app.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rrmchathura.cofee_order_app.Admin.Fragments.Coffee_Edit_Popup_Fragment;
import com.rrmchathura.cofee_order_app.Filters.CoffeeFilter;
import com.rrmchathura.cofee_order_app.Filters.CoffeeSettingsFilter;
import com.rrmchathura.cofee_order_app.Model.CoffeeModel;
import com.rrmchathura.cofee_order_app.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CoffeeSettingsAdapter extends RecyclerView.Adapter<CoffeeSettingsAdapter.CoffeeViewHolder> implements Filterable {

    Context context;
    public ArrayList<CoffeeModel> coffeeList,filterList;
    public CoffeeSettingsFilter filter;

    public CoffeeSettingsAdapter(Context context, ArrayList<CoffeeModel> coffeeList) {
        this.context = context;
        this.coffeeList = coffeeList;
        this.filterList = coffeeList;
    }

    @NonNull
    @Override
    public CoffeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.raw_menu_item_edit,parent,false);
        return new CoffeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoffeeViewHolder holder, int position) {

        CoffeeModel coffeeModel = coffeeList.get(position);

        String coffeeImage = coffeeModel.getCoffee_image();
        String coffeeName = coffeeModel.getCoffee_name();
        String coffeePrice = coffeeModel.getPrice();
        String isCustomizeAvailable = coffeeModel.getIsCustomizeCusAvailable();
        String Quantity = coffeeModel.getQuantity();
        String coffeeId = coffeeModel.getCoffeeId();

        try {
            Picasso.get().load(coffeeImage).placeholder(R.drawable.spinner).into(holder.coffeeImage);

        } catch (Exception e) {
            holder.coffeeImage.setImageResource(R.drawable.spinner);
        }

        holder.coffeeName.setText(coffeeName);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] options = {"Edit","Delete"};

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Select").setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0){

                            Bundle bundle = new Bundle();

                            bundle.putString("coffeeImage", coffeeImage);
                            bundle.putString("coffeeName", coffeeName);
                            bundle.putString("coffeePrice", coffeePrice);
                            bundle.putString("isCustomizeAvailable", isCustomizeAvailable);
                            bundle.putString("Quantity", Quantity);
                            bundle.putString("coffeeId", coffeeId);

                            Coffee_Edit_Popup_Fragment coffee_edit_popup_fragment = new Coffee_Edit_Popup_Fragment();
                            coffee_edit_popup_fragment.setArguments(bundle);
                            coffee_edit_popup_fragment.show(((AppCompatActivity) context).getSupportFragmentManager(), coffee_edit_popup_fragment.getClass().getSimpleName());
                            dialogInterface.dismiss();

                        }else {

                            dialogInterface.dismiss();
                            LoadDeleteDialog(coffeeId);


                        }
                    }
                });

                builder.show();

            }
        });

    }

    private void LoadDeleteDialog(String coffeeId) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("CoffeeMenu");

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("Delete Product");
        builder1.setMessage("You want to delete coffee?");
        builder1.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                databaseReference.child(coffeeId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context,"Coffee Deleted",Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                    }
                });
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder1.show();
    }

    @Override
    public int getItemCount() {
        return coffeeList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CoffeeSettingsFilter(this, filterList);
        }
        return filter;
    }

    public class CoffeeViewHolder extends RecyclerView.ViewHolder {

        ImageView coffeeImage;
        TextView coffeeName;
        ImageButton edit;

        public CoffeeViewHolder(@NonNull View itemView) {
            super(itemView);

            coffeeImage = itemView.findViewById(R.id.CoffeeImage);
            coffeeName = itemView.findViewById(R.id.coffeeName);
            edit = itemView.findViewById(R.id.edit);

        }
    }
}
