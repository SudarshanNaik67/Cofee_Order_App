package com.rrmchathura.cofee_order_app.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationBarView;
import com.rrmchathura.cofee_order_app.Admin.Fragments.Admin_Add_Menu_Fragment;
import com.rrmchathura.cofee_order_app.Admin.Fragments.Admin_Home_Fragment;
import com.rrmchathura.cofee_order_app.R;
import com.rrmchathura.cofee_order_app.databinding.ActivityAdminHomeBinding;

public class Admin_Home_Activity extends AppCompatActivity {

    ActivityAdminHomeBinding binding;

    Admin_Home_Fragment admin_home_fragment = new Admin_Home_Fragment();
    Admin_Add_Menu_Fragment admin_add_menu_fragment = new Admin_Add_Menu_Fragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager().beginTransaction().replace(R.id.container,admin_home_fragment).commit();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container,admin_add_menu_fragment).commit();
            }
        });

        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,admin_home_fragment).commit();
                        return true;

                }
                return false;
            }
        });

    }
}