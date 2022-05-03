package com.rrmchathura.cofee_order_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rrmchathura.cofee_order_app.Adapters.UserAdapters.ShowOrderDetailsAdapter;
import com.rrmchathura.cofee_order_app.Model.CartItemsModel;
import com.rrmchathura.cofee_order_app.databinding.ActivityShowOrderDetailsBinding;

import java.util.ArrayList;
import java.util.Calendar;

public class Show_Order_Details_Activity extends AppCompatActivity {

    ActivityShowOrderDetailsBinding binding;
    ArrayList<CartItemsModel> cartItemsModelList;
    ShowOrderDetailsAdapter showOrderDetailsAdapter;

    FirebaseDatabase database;
    FirebaseAuth mAuth;


    private String username, email, orderDate, amount, mobile, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowOrderDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        setSupportActionBar(binding.toolbar);

        Intent intent = getIntent();
        username = intent.getStringExtra("address");
        email = intent.getStringExtra("email");
        orderDate = intent.getStringExtra("orderDate");
        amount = intent.getStringExtra("amount");
        mobile = intent.getStringExtra("mobile");
        address = intent.getStringExtra("address");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(orderDate));
        String formatDate = DateFormat.format("dd/MM/yyyy hh:mm a", calendar).toString();

        binding.usernameTv.setText(username);
        binding.emailTv.setText(email);
        binding.orderDateTv.setText(formatDate);
        binding.mobileTv.setText(mobile);
        binding.amountTv.setText("Rs." + amount);
        binding.addressTv.setText(address);

    }

    @Override
    protected void onStart() {
        super.onStart();
        LoadOrderDetails();
    }

    private void LoadOrderDetails() {

        cartItemsModelList = new ArrayList<>();

        DatabaseReference databaseReference = database.getReference("Users");
        databaseReference.child(mAuth.getUid()).child("Orders").child(orderDate).child("Items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartItemsModelList.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    CartItemsModel cartItemsModel = ds.getValue(CartItemsModel.class);
                    cartItemsModelList.add(cartItemsModel);
                }

                showOrderDetailsAdapter = new ShowOrderDetailsAdapter(Show_Order_Details_Activity.this,cartItemsModelList);
                binding.nestedRecycleview.setAdapter(showOrderDetailsAdapter);
                showOrderDetailsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}