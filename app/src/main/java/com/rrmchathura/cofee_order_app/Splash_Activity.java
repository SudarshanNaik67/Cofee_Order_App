package com.rrmchathura.cofee_order_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rrmchathura.cofee_order_app.Admin.Admin_Home_Activity;
import com.rrmchathura.cofee_order_app.databinding.ActivitySplashBinding;

public class Splash_Activity extends AppCompatActivity {

    ActivitySplashBinding binding;
    FirebaseAuth mAuth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        String text = "COFFEE HUB";
        SpannableString ss = new SpannableString(text);
        ForegroundColorSpan fcsBlue = new ForegroundColorSpan(getResources().getColor(R.color.black));
        ForegroundColorSpan fcsYellow = new ForegroundColorSpan(getResources().getColor(R.color.brown));

        ss.setSpan(fcsBlue, 1, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(fcsYellow, 7, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        binding.text.setText(ss);


        Animation anim = AnimationUtils.loadAnimation(this, R.anim.splah_anim);
        binding.logo.setAnimation(anim);

        //   final Intent intent = new Intent(this,Welcome_Screen_Activity.class);
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    checkUser();
//                    startActivity(intent);
//                    finish();
                }
            }
        };
        timer.start();

    }

    private void checkUser() {

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser == null) {

            startActivity(new Intent(Splash_Activity.this, Welcome_Screen_Activity.class));
            finish();

        } else {
            DatabaseReference reference = database.getReference("Users");
            reference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String userType = "" + snapshot.child("usertype").getValue();


                    if (userType.equals("user")) {

                        startActivity(new Intent(Splash_Activity.this, MainActivity.class));
                        finish();

                    } else {
                        startActivity(new Intent(Splash_Activity.this, Admin_Home_Activity.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}