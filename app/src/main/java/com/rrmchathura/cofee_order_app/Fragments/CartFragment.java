package com.rrmchathura.cofee_order_app.Fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rrmchathura.cofee_order_app.Adapters.UserAdapters.CartItemsAdapter;
import com.rrmchathura.cofee_order_app.Adapters.UserAdapters.UserCoffeeAdapter;
import com.rrmchathura.cofee_order_app.Model.CartItemsModel;
import com.rrmchathura.cofee_order_app.R;
import com.rrmchathura.cofee_order_app.databinding.FragmentCartBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class CartFragment extends Fragment {

    FragmentCartBinding binding;
    ArrayList<CartItemsModel> cartItemsList;
    CartItemsAdapter cartItemsAdapter;
    CartItemsModel cartItemsModel;

    FirebaseDatabase database;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        databaseReference = database.getReference("Users");

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        binding.checkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveOrder();
            }
        });

        return binding.getRoot();
    }

    private void LoadCartData() {

        cartItemsList = new ArrayList<>();

        DatabaseReference databaseReference = database.getReference("Users");
        databaseReference.child(mAuth.getUid()).child("CartItems").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartItemsList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        cartItemsModel = ds.getValue(CartItemsModel.class);
                        cartItemsList.add(cartItemsModel);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SaveOrder() {

        progressDialog.setMessage("Saving order...");
        progressDialog.show();

        String timestamp = "" + System.currentTimeMillis();
        String TotalCost = binding.totalPriceCount.getText().toString().replace("Rs.", "");

        HashMap<String, Object> userdata = new HashMap<>();
        userdata.put("username", username);
        userdata.put("email", email);
        userdata.put("mobile", mobile);
        userdata.put("address", address);
        userdata.put("finalPrice", TotalCost);
        userdata.put("orderDate", timestamp);

        progressDialog.setMessage("Saving items");

        databaseReference.child(mAuth.getUid()).child("Orders").child(timestamp).setValue(userdata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                for (int i = 0; i < cartItemsList.size(); i++) {


                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("coffeeId", cartItemsList.get(i).getCoffeeId());
                    hashMap.put("coffeeImage", cartItemsList.get(i).getCoffeeImage());
                    hashMap.put("coffeeName", cartItemsList.get(i).getCoffeeName());
                    hashMap.put("finalPrice", cartItemsList.get(i).getFinalPrice());
                    hashMap.put("isCustomizeAvailable", cartItemsList.get(i).getIsCustomizeAvailable());
                    hashMap.put("quantity", cartItemsList.get(i).getQuantity());
                    hashMap.put("selectedAdditions", cartItemsList.get(i).getSelectedAdditions());
                    hashMap.put("selectedSize", cartItemsList.get(i).getSelectedSize());
                    hashMap.put("selectedSugar", cartItemsList.get(i).getSelectedSugar());
                    hashMap.put("timeStamp", cartItemsList.get(i).getTimeStamp());
                    hashMap.put("uid", cartItemsList.get(i).getUid());

                    databaseReference.child(mAuth.getUid()).child("Orders").child(timestamp).child("Items").child(cartItemsList.get(i).getTimeStamp()).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            removeCartData();
                        }
                    });
                }

            }
        });
    }

    private void removeCartData() {

        progressDialog.setMessage("Removing items");

            DatabaseReference databaseReference = database.getReference("Users");
            databaseReference.child(mAuth.getUid()).child("CartItems").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    progressDialog.dismiss();
                    cartItemsAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Order Successfully", Toast.LENGTH_SHORT).show();

                }
            });



    }

    @Override
    public void onStart() {
        super.onStart();
        LoadUserData();
        LoadCartItems();
        LoadTotalPrice();
        LoadCartData();
    }

    private String username, email, mobile, address;

    private void LoadUserData() {
        DatabaseReference databaseReference = database.getReference("Users");
        databaseReference.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username = "" + snapshot.child("username").getValue();
                email = "" + snapshot.child("email").getValue();
                mobile = "" + snapshot.child("mobile").getValue();
                address = "" + snapshot.child("address").getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void LoadTotalPrice() {

        DatabaseReference databaseReference = database.getReference("Users");
        databaseReference.child(mAuth.getUid()).child("CartItems").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double TotalPrice = 0;
                if (snapshot.exists()) {

                    binding.totalPriceCount.setVisibility(View.VISIBLE);
                    binding.checkOutBtn.setVisibility(View.VISIBLE);

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String finalPrice = "" + ds.child("finalPrice").getValue();

                        double value = Double.valueOf(finalPrice);
                        TotalPrice += value;

                        binding.totalPriceCount.setText(String.valueOf("Rs. " + TotalPrice));
                    }
                } else {
                    binding.totalPriceCount.setVisibility(View.GONE);
                    binding.checkOutBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void LoadCartItems() {

        cartItemsList = new ArrayList<>();

        DatabaseReference databaseReference = database.getReference("Users");
        databaseReference.child(mAuth.getUid()).child("CartItems").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartItemsList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        cartItemsModel = ds.getValue(CartItemsModel.class);
                        cartItemsList.add(cartItemsModel);
                    }

                    cartItemsAdapter = new CartItemsAdapter(getActivity(), cartItemsList);
                    binding.recycleview.setAdapter(cartItemsAdapter);
                    cartItemsAdapter.notifyDataSetChanged();
                }
                else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}