package com.rrmchathura.cofee_order_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rrmchathura.cofee_order_app.Fragments.CartFragment;
import com.rrmchathura.cofee_order_app.Fragments.HomeFragment;
import com.rrmchathura.cofee_order_app.Fragments.OrdersFragment;
import com.rrmchathura.cofee_order_app.Fragments.ProfileFragment;
import com.rrmchathura.cofee_order_app.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    HomeFragment homeFragment = new HomeFragment();
    CartFragment cartFragment = new CartFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    OrdersFragment ordersFragment = new OrdersFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();

        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
                        return true;

                    case R.id.cart:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, cartFragment).commit();
                        return true;

                    case R.id.profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
                        return true;

                    case R.id.orders:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, ordersFragment).commit();
                        return true;
                }

                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        BadgeDrawable badgeDrawable = binding.bottomNavigation.getOrCreateBadge(R.id.cart);
        LoadCartCount(badgeDrawable);

    }

    private void LoadCartCount(BadgeDrawable badgeDrawable) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(FirebaseAuth.getInstance().getUid()).child("CartItems").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    long count = snapshot.getChildrenCount();
                    badgeDrawable.setNumber((int) count);

                    if (count == 0){
                        badgeDrawable.setVisible(false);
                    }
                    else {
                        badgeDrawable.setVisible(true);
                        badgeDrawable.setBackgroundColor(getResources().getColor(R.color.brown));
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}