package com.rrmchathura.cofee_order_app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.rrmchathura.cofee_order_app.databinding.ActivityCoffeeDetailsBinding;
import com.squareup.picasso.Picasso;

public class Coffee_Details_Activity extends AppCompatActivity {

    ActivityCoffeeDetailsBinding binding;
    private String coffeeName,coffeePrice,coffeeImage;

    private int Quantity = 1;
    private int finalCost = 0;
    private int cost = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCoffeeDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        coffeeName = getIntent().getStringExtra("coffeeName");
        coffeePrice = getIntent().getStringExtra("coffeePrice");
        coffeeImage = getIntent().getStringExtra("coffeeImage");

        try {
            Picasso.get().load(coffeeImage).placeholder(R.drawable.spinner).into(binding.CoffeeImage);

        } catch (Exception e) {
            binding.CoffeeImage.setImageResource(R.drawable.spinner);
        }


        binding.coffeeName.setText(coffeeName);
        binding.coffeeNameTwo.setText(coffeeName);
        binding.priceTv.setText("Rs."+coffeePrice);
        binding.totalPriceTv.setText("Rs."+coffeePrice);

        cost = Integer.valueOf(coffeePrice);
        finalCost = Integer.parseInt(coffeePrice);

        binding.btnIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalCost = finalCost + cost;
                Quantity++;

                binding.totalPriceTv.setText("Rs."+String.valueOf(finalCost));
                binding.quantityTv.setText(String.valueOf(Quantity));
            }
        });

        binding.btnDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Quantity > 1) {
                    finalCost = finalCost - cost;
                    Quantity--;

                    binding.totalPriceTv.setText("Rs."+String.valueOf(finalCost));
                    binding.quantityTv.setText(String.valueOf(Quantity));
                }
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}