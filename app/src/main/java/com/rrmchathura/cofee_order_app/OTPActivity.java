package com.rrmchathura.cofee_order_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rrmchathura.cofee_order_app.databinding.ActivityOtpactivityBinding;

import java.util.HashMap;

public class OTPActivity extends AppCompatActivity {

    ActivityOtpactivityBinding binding;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);


        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.inputOtp1.getText().toString().trim().isEmpty() && !binding.inputOtp2.getText().toString().trim().isEmpty() && !binding.inputOtp3.getText().toString().trim().isEmpty()
                        && !binding.inputOtp4.getText().toString().trim().isEmpty() && !binding.inputOtp4.getText().toString().trim().isEmpty() && !binding.inputOtp5.getText().toString().trim().isEmpty() && !binding.inputOtp6.getText().toString().trim().isEmpty()) {

                    progressDialog.setMessage("Verifying Mobile.....");
                    progressDialog.show();

                    String enterdotp = binding.inputOtp1.getText().toString() +
                            binding.inputOtp2.getText().toString() +
                            binding.inputOtp3.getText().toString() +
                            binding.inputOtp4.getText().toString() +
                            binding.inputOtp1.getText().toString() +
                            binding.inputOtp1.getText().toString();

               //     binding.progressbarofotpauth.setVisibility(View.VISIBLE);
                    String codereciever = getIntent().getStringExtra("otp");
                    String countrycode1 = getIntent().getStringExtra("coutryCode");
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codereciever,enterdotp);
                    addphonenumbertodatabse();
                } else {
                    Toast.makeText(OTPActivity.this, "Enter All numbers", Toast.LENGTH_SHORT).show();
                }
            }
        });

        numberOtpMove();
    }

    private void addphonenumbertodatabse() {

        progressDialog.setMessage("updating status");
        progressDialog.show();

        HashMap<String,Object> obj = new HashMap<>();

        obj.put("isMobileVerified","true");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(FirebaseAuth.getInstance().getUid()).updateChildren(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(OTPActivity.this,"Mobile Verify Successfully",Toast.LENGTH_SHORT).show();
                finish();
            }
        });


    }

    private void numberOtpMove() {

        binding.inputOtp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    binding.inputOtp2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.inputOtp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    binding.inputOtp3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.inputOtp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    binding.inputOtp4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.inputOtp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    binding.inputOtp5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.inputOtp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    binding.inputOtp6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}