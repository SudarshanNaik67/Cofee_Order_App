package com.rrmchathura.cofee_order_app.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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
import com.rrmchathura.cofee_order_app.Adapters.UserAdapters.UserCoffeeAdapter;
import com.rrmchathura.cofee_order_app.Model.CoffeeModel;
import com.rrmchathura.cofee_order_app.R;
import com.rrmchathura.cofee_order_app.databinding.FragmentAdminHomeBinding;
import com.rrmchathura.cofee_order_app.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    FirebaseDatabase database;

    UserCoffeeAdapter userCoffeeAdapter;
    ArrayList<CoffeeModel> coffeeList;
    CoffeeModel coffeeModel;
    boolean searchEtVisible;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater,container,false);

        database = FirebaseDatabase.getInstance();

        binding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchEtVisible) {
                    binding.visibleLayout.setVisibility(View.GONE);
                    searchEtVisible = false;

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
                    userCoffeeAdapter.getFilter().filter(charSequence);
                } catch (Exception e) {
                    LoadAllCoffee();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    userCoffeeAdapter.getFilter().filter(editable);
                } catch (Exception e) {
                    LoadAllCoffee();
                }

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

                    userCoffeeAdapter = new UserCoffeeAdapter(getActivity(), coffeeList);
                    StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                    binding.recycleView.setLayoutManager(staggeredGridLayoutManager);
                    binding.recycleView.setAdapter(userCoffeeAdapter);
                    userCoffeeAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}