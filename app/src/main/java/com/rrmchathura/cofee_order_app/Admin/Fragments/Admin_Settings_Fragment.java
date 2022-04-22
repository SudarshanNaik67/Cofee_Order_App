package com.rrmchathura.cofee_order_app.Admin.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rrmchathura.cofee_order_app.Adapters.CoffeeAdapter;
import com.rrmchathura.cofee_order_app.Adapters.CoffeeSettingsAdapter;
import com.rrmchathura.cofee_order_app.Model.CoffeeModel;
import com.rrmchathura.cofee_order_app.R;
import com.rrmchathura.cofee_order_app.databinding.FragmentAdminSettingsBinding;

import java.util.ArrayList;

public class Admin_Settings_Fragment extends Fragment {

    FragmentAdminSettingsBinding binding;

    CoffeeModel coffeeModel;
    boolean searchEtVisible;
    CoffeeSettingsAdapter coffeeSettingsAdapter;
    ArrayList<CoffeeModel> coffeeList;

    FirebaseDatabase database;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdminSettingsBinding.inflate(inflater,container,false);

        database = FirebaseDatabase.getInstance();

        binding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchEtVisible) {

                    binding.visibleLayout.setVisibility(View.GONE);
                    searchEtVisible = false;
                    LoadAllCoffee();

                } else {
                    binding.visibleLayout.setVisibility(View.VISIBLE);
                    searchEtVisible = true;
                }
            }
        });

        binding.searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    coffeeSettingsAdapter.getFilter().filter(charSequence);
                } catch (Exception e) {
                    LoadAllCoffee();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        LoadAllCoffee();
    }

    private void LoadAllCoffee() {

        coffeeList = new ArrayList<>();

        DatabaseReference databaseReference = database.getReference("CoffeeMenu");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                coffeeList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        coffeeModel = ds.getValue(CoffeeModel.class);
                        coffeeList.add(coffeeModel);
                    }

                    coffeeSettingsAdapter = new CoffeeSettingsAdapter(getActivity(), coffeeList);
                    binding.recycleView.setAdapter(coffeeSettingsAdapter);
                    coffeeSettingsAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}