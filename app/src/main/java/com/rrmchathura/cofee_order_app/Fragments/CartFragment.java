package com.rrmchathura.cofee_order_app.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class CartFragment extends Fragment {

    FragmentCartBinding binding;
    ArrayList<CartItemsModel> cartItemsList;
    CartItemsAdapter cartItemsAdapter;
    CartItemsModel cartItemsModel;

    FirebaseDatabase database;
    FirebaseAuth mAuth;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater,container,false);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        LoadCartItems();
        LoadTotalPrice();
    }

    private void LoadTotalPrice() {

        DatabaseReference databaseReference = database.getReference("Users");
        databaseReference.child(mAuth.getUid()).child("CartItems").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double TotalPrice = 0;
                if (snapshot.exists()){

                    binding.totalPriceCount.setVisibility(View.VISIBLE);

                    for (DataSnapshot ds : snapshot.getChildren()){
                        String finalPrice = ""+ds.child("finalPrice").getValue();

                        double value = Double.valueOf(finalPrice);
                        TotalPrice += value;

                        binding.totalPriceCount.setText(String.valueOf("Rs. " + TotalPrice));
                    }
                }
                else {
                    binding.totalPriceCount.setVisibility(View.GONE);
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
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartItemsList.clear();
                if (snapshot.exists()){
                    for (DataSnapshot ds : snapshot.getChildren()){
                        cartItemsModel = ds.getValue(CartItemsModel.class);
                        cartItemsList.add(cartItemsModel);

                    }

                    cartItemsAdapter = new CartItemsAdapter(getActivity(), cartItemsList);
                    binding.recycleview.setAdapter(cartItemsAdapter);
                    cartItemsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}