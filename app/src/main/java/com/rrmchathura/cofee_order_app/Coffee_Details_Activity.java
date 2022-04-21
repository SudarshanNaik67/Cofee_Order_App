package com.rrmchathura.cofee_order_app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.rrmchathura.cofee_order_app.databinding.ActivityCoffeeDetailsBinding;
import com.squareup.picasso.Picasso;

public class Coffee_Details_Activity extends AppCompatActivity {

    ActivityCoffeeDetailsBinding binding;
    private String coffeeName, coffeePrice, coffeeImage;
    private String isCustomizeAvailable;

    private int Quantity = 1;
    private int finalCost = 0;
    private int cost = 0;

    private double smallSizeCost, mediumSizeCost, largeSizeCost, noSugarCost, smallSugarCost, mediumSugarCost, highSugarCost, additionalCreamCost, additionalStickerCost;
    private double finalSizeCost, finalSugarCost, finalAdditionalCost;

    boolean smallSizeChecked = false;
    boolean mediumSizeChecked = false;
    boolean largeSizeChecked = false;

    boolean noSugarChecked = false;
    boolean smallSugarChecked = false;
    boolean mediumSugarChecked = false;
    boolean highSugarChecked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCoffeeDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        coffeeName = getIntent().getStringExtra("coffeeName");
        coffeePrice = getIntent().getStringExtra("coffeePrice");
        coffeeImage = getIntent().getStringExtra("coffeeImage");
        isCustomizeAvailable = getIntent().getStringExtra("isCustomizeAvailable");

        try {
            Picasso.get().load(coffeeImage).placeholder(R.drawable.spinner).into(binding.CoffeeImage);

        } catch (Exception e) {
            binding.CoffeeImage.setImageResource(R.drawable.spinner);
        }


        binding.coffeeName.setText(coffeeName);
        binding.coffeeNameTwo.setText(coffeeName);
        binding.priceTv.setText("Rs." + coffeePrice);
        binding.totalPriceTv.setText("Rs." + coffeePrice);

        if (isCustomizeAvailable.equals("true")) {

            binding.mainRl.setVisibility(View.VISIBLE);
            binding.sugarMainRl.setVisibility(View.VISIBLE);
            binding.additionMainRl.setVisibility(View.VISIBLE);
        } else {
            binding.mainRl.setVisibility(View.GONE);
            binding.view2.setVisibility(View.GONE);
            binding.sugarMainRl.setVisibility(View.GONE);
            binding.view3.setVisibility(View.GONE);
            binding.additionMainRl.setVisibility(View.GONE);
            binding.view4.setVisibility(View.GONE);
        }

        cost = Integer.valueOf(coffeePrice);
        finalCost = Integer.valueOf(coffeePrice);

        binding.btnIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                binding.radioGroup2.clearCheck();
                binding.radioGroup3.clearCheck();
                binding.radioGroup4.clearCheck();

                finalCost = finalCost + cost;
                Quantity++;

                binding.totalPriceTv.setText("Rs." + "" + finalCost);
                binding.quantityTv.setText("" + Quantity);


            }
        });

        binding.btnDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                binding.radioGroup2.clearCheck();
                binding.radioGroup3.clearCheck();
                binding.radioGroup4.clearCheck();

                if (Quantity > 1) {
                    finalCost = finalCost - cost;
                    Quantity--;

                    binding.totalPriceTv.setText("Rs." + "" + finalCost);
                    binding.quantityTv.setText(String.valueOf(Quantity));
                }


            }
        });

        binding.smallSizeRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                binding.radioGroup3.clearCheck();
                binding.radioGroup4.clearCheck();

                smallSizeChecked = true;
                mediumSizeChecked = false;
                largeSizeChecked = false;

                smallSizeCost = finalCost * 0.1;
                finalSizeCost = finalCost + smallSizeCost;
                binding.totalPriceTv.setText("Rs." + "" + finalSizeCost);

            }
        });

        binding.mediumSizeRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                smallSizeChecked = false;
                mediumSizeChecked = true;
                largeSizeChecked = false;

                binding.radioGroup3.clearCheck();
                binding.radioGroup4.clearCheck();

                mediumSizeCost = finalCost * 0.2;
                finalSizeCost = finalCost + mediumSizeCost;
                binding.totalPriceTv.setText("Rs." + "" + finalSizeCost);


            }
        });

        binding.largeSizeRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                smallSizeChecked = false;
                mediumSizeChecked = false;
                largeSizeChecked = true;

                binding.radioGroup3.clearCheck();
                binding.radioGroup4.clearCheck();

                largeSizeCost = finalCost * 0.3;
                finalSizeCost = finalCost + largeSizeCost;
                binding.totalPriceTv.setText("Rs." + "" + finalSizeCost);
            }
        });

        binding.noSugarRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                highSugarChecked = false;
                noSugarChecked = true;
                smallSugarChecked = false;
                mediumSugarChecked = false;

                if (isCustomizeAvailable.equals("true")) {

                    if (smallSizeChecked || mediumSizeChecked || largeSizeChecked) {

                        noSugarCost = finalCost * (-0.1); //-10%
                        finalSugarCost = finalSizeCost + noSugarCost;
                        binding.totalPriceTv.setText("Rs." + ""+finalSugarCost);

                        binding.radioGroup3.clearCheck();
                        binding.radioGroup4.clearCheck();
                        binding.noSugarRB.setChecked(true);
                    } else {
                        Toast.makeText(Coffee_Details_Activity.this, "Please Select Size", Toast.LENGTH_SHORT).show();
                        binding.noSugarRB.setChecked(false);
                    }


                } else {

                    noSugarCost = finalCost * (-0.1); //-10%
                    finalSugarCost = finalCost + noSugarCost;
                    binding.totalPriceTv.setText("Rs." + ""+finalSugarCost);

                }
            }
        });

        binding.smallSugarRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                highSugarChecked = false;
                noSugarChecked = false;
                smallSugarChecked = true;
                mediumSugarChecked = false;

                if (isCustomizeAvailable.equals("true")) {

                    if (smallSizeChecked || mediumSizeChecked || largeSizeChecked) {

                        smallSugarCost = finalCost * 0.01; //-10%
                        finalSugarCost = finalSizeCost + smallSugarCost;
                        binding.totalPriceTv.setText("Rs." + ""+finalSugarCost);

                        binding.radioGroup3.clearCheck();
                        binding.radioGroup4.clearCheck();
                        binding.smallSugarRB.setChecked(true);
                    } else {
                        Toast.makeText(Coffee_Details_Activity.this, "Please Select Size", Toast.LENGTH_SHORT).show();
                        binding.smallSugarRB.setChecked(false);
                    }


                } else {

                    smallSugarCost = finalCost * 0.01; //-10%
                    finalSugarCost = finalCost + smallSugarCost;
                    binding.totalPriceTv.setText("Rs." + ""+finalSugarCost);

                }

            }
        });

        binding.mediumSugarRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                highSugarChecked = false;
                noSugarChecked = false;
                smallSugarChecked = false;
                mediumSugarChecked = true;

                if (isCustomizeAvailable.equals("true")) {

                    if (smallSizeChecked || mediumSizeChecked || largeSizeChecked) {

                        mediumSugarCost = finalCost * 0.02; //-10%
                        finalSugarCost = finalSizeCost + mediumSugarCost;
                        binding.totalPriceTv.setText("Rs." + ""+finalSugarCost);

                        binding.radioGroup3.clearCheck();
                        binding.radioGroup4.clearCheck();
                        binding.mediumSugarRB.setChecked(true);
                    } else {
                        Toast.makeText(Coffee_Details_Activity.this, "Please Select Size", Toast.LENGTH_SHORT).show();
                        binding.mediumSugarRB.setChecked(false);
                    }


                } else {

                    mediumSugarCost = finalCost * 0.02; //-10%
                    finalSugarCost = finalCost + mediumSugarCost;
                    binding.totalPriceTv.setText("Rs." + ""+finalSugarCost);

                }
            }
        });

        binding.highSugarRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                highSugarChecked = true;
                noSugarChecked = false;
                smallSugarChecked = false;
                mediumSugarChecked = false;

                if (isCustomizeAvailable.equals("true")) {

                    if (smallSizeChecked || mediumSizeChecked || largeSizeChecked) {

                        highSugarCost = finalCost * 0.03; //-10%
                        finalSugarCost = finalSizeCost + highSugarCost;
                        binding.totalPriceTv.setText("Rs." + ""+finalSugarCost);

                        binding.radioGroup3.clearCheck();
                        binding.radioGroup4.clearCheck();
                        binding.highSugarRB.setChecked(true);
                    } else {
                        Toast.makeText(Coffee_Details_Activity.this, "Please Select Size", Toast.LENGTH_SHORT).show();
                        binding.highSugarRB.setChecked(false);
                    }


                } else {

                    highSugarCost = finalCost * 0.03; //-10%
                    finalSugarCost = finalCost + highSugarCost;
                    binding.totalPriceTv.setText("Rs." + ""+finalSugarCost);

                }
            }
        });

        binding.additionCreamRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (isCustomizeAvailable.equals("true")) {

                    if (noSugarChecked || smallSugarChecked || mediumSugarChecked || highSugarChecked) {

                        additionalCreamCost = finalCost * 0.05;
                        finalAdditionalCost = finalSugarCost + additionalCreamCost;
                        binding.totalPriceTv.setText("Rs." + ""+finalAdditionalCost);

                        binding.radioGroup4.clearCheck();
                        binding.additionCreamRB.setChecked(true);
                    } else {
                        Toast.makeText(Coffee_Details_Activity.this, "Please Select Sugar", Toast.LENGTH_SHORT).show();
                        binding.additionCreamRB.setChecked(false);
                    }


                } else {

                    additionalCreamCost = finalCost * 0.05; //-10%
                    finalAdditionalCost = finalSugarCost + additionalCreamCost;
                    binding.totalPriceTv.setText("Rs." + ""+finalAdditionalCost);

                }

            }
        });

        binding.additionalStickerRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCustomizeAvailable.equals("true")) {

                    if (noSugarChecked || smallSugarChecked || mediumSugarChecked || highSugarChecked) {


                        additionalStickerCost = finalCost * 0.01;
                        finalAdditionalCost = finalSugarCost + additionalStickerCost;
                        binding.totalPriceTv.setText("Rs." + ""+finalAdditionalCost);

                        binding.radioGroup4.clearCheck();
                        binding.additionalStickerRB.setChecked(true);
                    } else {
                        Toast.makeText(Coffee_Details_Activity.this, "Please Select Sugar", Toast.LENGTH_SHORT).show();
                        binding.additionalStickerRB.setChecked(false);
                    }


                } else {


                    additionalStickerCost = finalCost * 0.01;
                    finalAdditionalCost = finalSugarCost + additionalStickerCost;
                    binding.totalPriceTv.setText("Rs." + ""+finalAdditionalCost);

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