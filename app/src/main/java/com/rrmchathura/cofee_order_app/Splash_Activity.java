package com.rrmchathura.cofee_order_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.rrmchathura.cofee_order_app.databinding.ActivitySplashBinding;

public class Splash_Activity extends AppCompatActivity {

    ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String text = "COFFEE HUB";
        SpannableString ss = new SpannableString(text);
        ForegroundColorSpan fcsBlue = new ForegroundColorSpan(getResources().getColor(R.color.black));
        ForegroundColorSpan fcsYellow = new ForegroundColorSpan(getResources().getColor(R.color.brown));

        ss.setSpan(fcsBlue,1,6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(fcsYellow,7,10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        binding.text.setText(ss);


        Animation anim = AnimationUtils.loadAnimation(this,R.anim.splah_anim);
        binding.logo.setAnimation(anim);

        final Intent intent = new Intent(this,Welcome_Screen_Activity.class);
        Thread timer = new Thread(){
            public void run(){
                try {
                    sleep(2000);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    startActivity(intent);
                    finish();
                }
            }
        };
        timer.start();

    }
}