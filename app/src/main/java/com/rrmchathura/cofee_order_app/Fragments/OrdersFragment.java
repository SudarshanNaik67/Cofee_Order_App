package com.rrmchathura.cofee_order_app.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rrmchathura.cofee_order_app.Adapters.UserAdapters.ShowOrdersAdapter;
import com.rrmchathura.cofee_order_app.Model.OrdersModel;
import com.rrmchathura.cofee_order_app.R;
import com.rrmchathura.cofee_order_app.databinding.FragmentOrdersBinding;

import java.util.ArrayList;

public class OrdersFragment extends Fragment {

    FragmentOrdersBinding binding;
    FirebaseAuth mAuth;
    FirebaseDatabase database;

    ShowOrdersAdapter showOrdersAdapter;
    ArrayList<OrdersModel> ordersList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrdersBinding.inflate(inflater,container,false);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        LoadOrders();
    }

    private void LoadOrders() {

        ordersList = new ArrayList<>();

        DatabaseReference databaseReference = database.getReference("Users");
        databaseReference.child(mAuth.getUid()).child("Orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ordersList.clear();
                if (snapshot.exists()){

                    binding.lottie.setVisibility(View.GONE);
                    binding.recycleview.setVisibility(View.VISIBLE);

                    for (DataSnapshot ds : snapshot.getChildren()){
                        OrdersModel ordersModel = ds.getValue(OrdersModel.class);
                        ordersList.add(ordersModel);

                    }
                }
                else {
                    binding.lottie.setVisibility(View.VISIBLE);
                    binding.recycleview.setVisibility(View.VISIBLE);
                }

                showOrdersAdapter = new ShowOrdersAdapter(getActivity(),ordersList);
                binding.recycleview.setAdapter(showOrdersAdapter);
                showOrdersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}