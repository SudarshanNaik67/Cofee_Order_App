package com.rrmchathura.cofee_order_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rrmchathura.cofee_order_app.databinding.ActivityUserCoffeeDetailsBinding;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class User_Coffee_Details_Activity extends AppCompatActivity {

    ActivityUserCoffeeDetailsBinding binding;
    private String coffeeName, coffeePrice, coffeeImage, isCustomizeAvailable, coffeeId;

    private int Quantity = 1;
    private int finalCost = 0;
    private int cost = 0;

    boolean smallSizeChecked = false;
    boolean mediumSizeChecked = false;
    boolean largeSizeChecked = false;

    FirebaseAuth mAuth;
    FirebaseDatabase database;

    boolean noSugarChecked = false;
    boolean smallSugarChecked = false;
    boolean mediumSugarChecked = false;
    boolean highSugarChecked = false;

    private double smallSizeCost, mediumSizeCost, largeSizeCost, noSugarCost, smallSugarCost, mediumSugarCost, highSugarCost, additionalCreamCost, additionalStickerCost;
    private double finalSizeCost, finalSugarCost, finalAdditionalCost;

    private String selectedSize, selectedSugar;
    private String selectedAdditions = "empty";

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserCoffeeDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        setSupportActionBar(binding.toolbar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);

        coffeeName = getIntent().getStringExtra("coffeeName");
        coffeePrice = getIntent().getStringExtra("coffeePrice");
        coffeeImage = getIntent().getStringExtra("coffeeImage");
        coffeeId = getIntent().getStringExtra("coffeeId");
        isCustomizeAvailable = getIntent().getStringExtra("isCustomizeAvailable");

        try {
            Picasso.get().load(coffeeImage).placeholder(R.drawable.spinner).into(binding.CoffeeImage);

        } catch (Exception e) {
            binding.CoffeeImage.setImageResource(R.drawable.spinner);
        }


        binding.coffeeNameEt.setText(coffeeName);
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

                selectedSize = "small";

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

                selectedSize = "medium";

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

                selectedSize = "large";

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

                selectedSugar = "noSugar";

                highSugarChecked = false;
                noSugarChecked = true;
                smallSugarChecked = false;
                mediumSugarChecked = false;

                if (isCustomizeAvailable.equals("true")) {

                    if (smallSizeChecked || mediumSizeChecked || largeSizeChecked) {

                        noSugarCost = finalCost * (-0.1); //-10%
                        finalSugarCost = finalSizeCost + noSugarCost;
                        binding.totalPriceTv.setText("Rs." + "" + finalSugarCost);

                        binding.radioGroup3.clearCheck();
                        binding.radioGroup4.clearCheck();
                        binding.noSugarRB.setChecked(true);
                    } else {
                        Toast.makeText(User_Coffee_Details_Activity.this, "Please Select Size", Toast.LENGTH_SHORT).show();
                        binding.noSugarRB.setChecked(false);
                    }


                } else {

                    noSugarCost = finalCost * (-0.1); //-10%
                    finalSugarCost = finalCost + noSugarCost;
                    binding.totalPriceTv.setText("Rs." + "" + finalSugarCost);

                }
            }
        });

        binding.smallSugarRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedSugar = "smallSugar";

                highSugarChecked = false;
                noSugarChecked = false;
                smallSugarChecked = true;
                mediumSugarChecked = false;

                if (isCustomizeAvailable.equals("true")) {

                    if (smallSizeChecked || mediumSizeChecked || largeSizeChecked) {

                        smallSugarCost = finalCost * 0.01; //-10%
                        finalSugarCost = finalSizeCost + smallSugarCost;
                        binding.totalPriceTv.setText("Rs." + "" + finalSugarCost);

                        binding.radioGroup3.clearCheck();
                        binding.radioGroup4.clearCheck();
                        binding.smallSugarRB.setChecked(true);
                    } else {
                        Toast.makeText(User_Coffee_Details_Activity.this, "Please Select Size", Toast.LENGTH_SHORT).show();
                        binding.smallSugarRB.setChecked(false);
                    }


                } else {

                    smallSugarCost = finalCost * 0.01; //-10%
                    finalSugarCost = finalCost + smallSugarCost;
                    binding.totalPriceTv.setText("Rs." + "" + finalSugarCost);

                }

            }
        });

        binding.mediumSugarRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedSugar = "mediumSugar";

                highSugarChecked = false;
                noSugarChecked = false;
                smallSugarChecked = false;
                mediumSugarChecked = true;

                if (isCustomizeAvailable.equals("true")) {

                    if (smallSizeChecked || mediumSizeChecked || largeSizeChecked) {

                        mediumSugarCost = finalCost * 0.02; //-10%
                        finalSugarCost = finalSizeCost + mediumSugarCost;
                        binding.totalPriceTv.setText("Rs." + "" + finalSugarCost);

                        binding.radioGroup3.clearCheck();
                        binding.radioGroup4.clearCheck();
                        binding.mediumSugarRB.setChecked(true);
                    } else {
                        Toast.makeText(User_Coffee_Details_Activity.this, "Please Select Size", Toast.LENGTH_SHORT).show();
                        binding.mediumSugarRB.setChecked(false);
                    }


                } else {

                    mediumSugarCost = finalCost * 0.02; //-10%
                    finalSugarCost = finalCost + mediumSugarCost;
                    binding.totalPriceTv.setText("Rs." + "" + finalSugarCost);

                }
            }
        });

        binding.highSugarRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedSugar = "highSugar";

                highSugarChecked = true;
                noSugarChecked = false;
                smallSugarChecked = false;
                mediumSugarChecked = false;

                if (isCustomizeAvailable.equals("true")) {

                    if (smallSizeChecked || mediumSizeChecked || largeSizeChecked) {

                        highSugarCost = finalCost * 0.03; //-10%
                        finalSugarCost = finalSizeCost + highSugarCost;
                        binding.totalPriceTv.setText("Rs." + "" + finalSugarCost);

                        binding.radioGroup3.clearCheck();
                        binding.radioGroup4.clearCheck();
                        binding.highSugarRB.setChecked(true);
                    } else {
                        Toast.makeText(User_Coffee_Details_Activity.this, "Please Select Size", Toast.LENGTH_SHORT).show();
                        binding.highSugarRB.setChecked(false);
                    }


                } else {

                    highSugarCost = finalCost * 0.03; //-10%
                    finalSugarCost = finalCost + highSugarCost;
                    binding.totalPriceTv.setText("Rs." + "" + finalSugarCost);

                }
            }
        });

        binding.additionCreamRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedAdditions = "cream";


                if (isCustomizeAvailable.equals("true")) {

                    if (noSugarChecked || smallSugarChecked || mediumSugarChecked || highSugarChecked) {

                        additionalCreamCost = finalCost * 0.05;
                        finalAdditionalCost = finalSugarCost + additionalCreamCost;
                        binding.totalPriceTv.setText("Rs." + "" + finalAdditionalCost);

                        binding.radioGroup4.clearCheck();
                        binding.additionCreamRB.setChecked(true);
                    } else {
                        Toast.makeText(User_Coffee_Details_Activity.this, "Please Select Sugar", Toast.LENGTH_SHORT).show();
                        binding.additionCreamRB.setChecked(false);
                    }


                } else {

                    additionalCreamCost = finalCost * 0.05; //-10%
                    finalAdditionalCost = finalSugarCost + additionalCreamCost;
                    binding.totalPriceTv.setText("Rs." + "" + finalAdditionalCost);

                }

            }
        });

        binding.additionalStickerRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedAdditions = "sticker";

                if (isCustomizeAvailable.equals("true")) {

                    if (noSugarChecked || smallSugarChecked || mediumSugarChecked || highSugarChecked) {


                        additionalStickerCost = finalCost * 0.01;
                        finalAdditionalCost = finalSugarCost + additionalStickerCost;
                        binding.totalPriceTv.setText("Rs." + "" + finalAdditionalCost);

                        binding.radioGroup4.clearCheck();
                        binding.additionalStickerRB.setChecked(true);
                    } else {
                        Toast.makeText(User_Coffee_Details_Activity.this, "Please Select Sugar", Toast.LENGTH_SHORT).show();
                        binding.additionalStickerRB.setChecked(false);
                    }


                } else {


                    additionalStickerCost = finalCost * 0.01;
                    finalAdditionalCost = finalSugarCost + additionalStickerCost;
                    binding.totalPriceTv.setText("Rs." + "" + finalAdditionalCost);

                }

            }
        });

        binding.purchaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(address) && TextUtils.isEmpty(mobile)) {
                    Toast.makeText(User_Coffee_Details_Activity.this, "Please update address & mobile", Toast.LENGTH_SHORT).show();
                } else {
                    String finalQuantity = binding.quantityTv.getText().toString();

                    if (isCustomizeAvailable.equals("true")) {

                        if ((smallSizeChecked || mediumSizeChecked || largeSizeChecked) && (noSugarChecked || smallSugarChecked || mediumSugarChecked || highSugarChecked)) {
                            String finalCostOfCoffee = binding.totalPriceTv.getText().toString().replace("Rs.", "");
                            dialogBox(finalCostOfCoffee, finalQuantity);
                        } else {
                            Toast.makeText(User_Coffee_Details_Activity.this, "Please select at least Size & Sugar", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        String finalCostOfCoffee = binding.totalPriceTv.getText().toString().replace("Rs.", "");
                        dialogBox(finalCostOfCoffee, finalQuantity);
                    }

                }


            }
        });


    }

    private void dialogBox(String finalCostOfCoffee, String finalQuantity) {

        AlertDialog alertDialog = new AlertDialog.Builder(User_Coffee_Details_Activity.this).create();
        alertDialog.setTitle("Confirm Order");
        alertDialog.setMessage("You Need To Pay " + "Rs." + finalCostOfCoffee);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                AddCoffeeToCart(finalCostOfCoffee, finalQuantity, alertDialog);
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void AddCoffeeToCart(String finalCostOfCoffee, String finalQuantity, AlertDialog alertDialog) {

        progressDialog.setMessage("Coffee adding to cart");
        progressDialog.show();


        DatabaseReference databaseReference = database.getReference("Users");

        String timeStamp = String.valueOf(System.currentTimeMillis());

        if (isCustomizeAvailable.equals("true")) {

            if (selectedAdditions.equals("empty")) {

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("coffeeId", coffeeId);
                hashMap.put("coffeeName", coffeeName);
                hashMap.put("coffeeImage", "" + coffeeImage);
                hashMap.put("quantity", finalQuantity);
                hashMap.put("selectedSize", selectedSize);
                hashMap.put("selectedSugar", selectedSugar);
                hashMap.put("selectedAdditions", "empty");
                hashMap.put("timeStamp", "" + timeStamp);
                hashMap.put("isCustomizeAvailable", "" + isCustomizeAvailable);
                hashMap.put("finalPrice", "" + finalCostOfCoffee);
                hashMap.put("uid", "" + mAuth.getUid());

                databaseReference.child(mAuth.getUid()).child("CartItems").child(timeStamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        alertDialog.dismiss();
                        progressDialog.dismiss();
                        Toast.makeText(User_Coffee_Details_Activity.this, "Coffee added to cart..", Toast.LENGTH_SHORT).show();
                        ClearData();

                    }
                });

            } else {

                HashMap<String, Object> hashMap1 = new HashMap<>();
                hashMap1.put("coffeeId", coffeeId);
                hashMap1.put("coffeeName", coffeeName);
                hashMap1.put("coffeeImage", "" + coffeeImage);
                hashMap1.put("quantity", finalQuantity);
                hashMap1.put("selectedSize", selectedSize);
                hashMap1.put("timeStamp", "" + timeStamp);
                hashMap1.put("selectedSugar", selectedSugar);
                hashMap1.put("selectedAdditions", selectedAdditions);
                hashMap1.put("isCustomizeAvailable", "" + isCustomizeAvailable);
                hashMap1.put("finalPrice", "" + finalCostOfCoffee);
                hashMap1.put("uid", "" + mAuth.getUid());

                databaseReference.child(mAuth.getUid()).child("CartItems").child(timeStamp).setValue(hashMap1).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        alertDialog.dismiss();
                        progressDialog.dismiss();
                        Toast.makeText(User_Coffee_Details_Activity.this, "Coffee added to cart..", Toast.LENGTH_SHORT).show();
                        ClearData();

                    }
                });
                alertDialog.dismiss();
            }
        } else {
            HashMap<String, Object> hashMap2 = new HashMap<>();
            hashMap2.put("coffeeId", coffeeId);
            hashMap2.put("coffeeName", coffeeName);
            hashMap2.put("coffeeImage", "" + coffeeImage);
            hashMap2.put("quantity", finalQuantity);
            hashMap2.put("isCustomizeAvailable", "" + isCustomizeAvailable);
            hashMap2.put("selectedSize", "empty");
            hashMap2.put("timeStamp", "" + timeStamp);
            hashMap2.put("selectedSugar", "empty");
            hashMap2.put("selectedAdditions", "empty");
            hashMap2.put("finalPrice", "" + finalCostOfCoffee);
            hashMap2.put("uid", "" + mAuth.getUid());

            databaseReference.child(mAuth.getUid()).child("CartItems").child(timeStamp).setValue(hashMap2).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    alertDialog.dismiss();
                    progressDialog.dismiss();
                    Toast.makeText(User_Coffee_Details_Activity.this, "Coffee added to cart..", Toast.LENGTH_SHORT).show();
                    ClearData();
                }
            });
        }
    }

    private void ClearData() {
        binding.radioGroup2.clearCheck();
        binding.radioGroup3.clearCheck();
        binding.radioGroup4.clearCheck();
        binding.quantityTv.setText("1");
        binding.totalPriceTv.setText("Rs." + coffeePrice);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LoadUserData();
    }

    private String address = "", mobile = "";

    private void LoadUserData() {
        DatabaseReference databaseReference = database.getReference("Users");
        databaseReference.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    address = "" + snapshot.child("address").getValue();
                    mobile = "" + snapshot.child("mobile").getValue();
                } else
                {}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}