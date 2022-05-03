package com.rrmchathura.cofee_order_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rrmchathura.cofee_order_app.databinding.ActivityWelcomeScreenBinding;

import java.util.HashMap;

public class Welcome_Screen_Activity extends AppCompatActivity {

    ActivityWelcomeScreenBinding binding;

    private static final int RC_SIGN_IN = 65;
    GoogleSignInClient mGoogleSignInClient;

    FirebaseAuth mAuth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        // Configure Google Sign In
        GoogleSignInOptions gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(Welcome_Screen_Activity.this, gso);

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Welcome_Screen_Activity.this,LoginActivity.class));
            }
        });

        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Welcome_Screen_Activity.this,RegisterActivity.class));
            }
        });

        binding.googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuthWithGoogle(account.getIdToken());

            } catch (ApiException e) {
                Toast.makeText(Welcome_Screen_Activity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                Log.d("GOOGLEFAILED",e.getMessage());
                // Google Sign In failed, update UI appropriately

            }
        }

    }

    private void firebaseAuthWithGoogle(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        FirebaseUser user = mAuth.getCurrentUser();

                        //if user is signing in first time then get and show user info from google account
                        if (task.getResult().getAdditionalUserInfo().isNewUser()){
                            String email  = user.getEmail();
                            String uid  = user.getUid();
                            String username = user.getDisplayName();
                            String image = user.getPhotoUrl().toString();
                            long timestamp = System.currentTimeMillis();


                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("username",username);
                            hashMap.put("email",email);
                            hashMap.put("uid",""+uid);
                            hashMap.put("usertype","user");
                            hashMap.put("register_date",""+timestamp);
                            hashMap.put("profile_pic",""+image);


                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Users");
                            reference.child(uid).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(Welcome_Screen_Activity.this, "Google sign in successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        startActivity(new Intent(Welcome_Screen_Activity.this, MainActivity.class));
                        finish();
                    } else {

                    }
                }).addOnFailureListener(e -> Toast.makeText(Welcome_Screen_Activity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show());
    }
}