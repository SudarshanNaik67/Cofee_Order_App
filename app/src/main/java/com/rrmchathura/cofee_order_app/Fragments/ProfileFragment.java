package com.rrmchathura.cofee_order_app.Fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;
import com.rrmchathura.cofee_order_app.OTPActivity;
import com.rrmchathura.cofee_order_app.R;
import com.rrmchathura.cofee_order_app.Splash_Activity;
import com.rrmchathura.cofee_order_app.databinding.FragmentProfileBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    FirebaseStorage storageReference;

    ProgressDialog progressDialog;
    Dialog dialog;

    private String[] cameraPermissions;
    private String[] storagePermissions;

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;

    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;

    private Uri image_uri;
    private String countryCode, phoneNumber, codeSent;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);

        countryCode = binding.countrycodepicker.getSelectedCountryCodeWithPlus();

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        LoadUserData();

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadLogoutDialogBox();
            }
        });

        binding.selectPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickerDialog();
            }
        });

        binding.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateData();
            }
        });

        binding.verifyMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number;
                number = binding.mobileEt.getText().toString();
                if (number.isEmpty()) {
                    Toast.makeText(getContext(), "Please Enter Your Number", Toast.LENGTH_SHORT).show();
                } else if (number.length() < 10) {
                    Toast.makeText(getContext(), "Please Enter Correct Number", Toast.LENGTH_SHORT).show();
                } else {

                    progressDialog.setMessage("Sending OTP.....");
                    progressDialog.show();

                    phoneNumber = countryCode + number;

                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth).setPhoneNumber(phoneNumber)
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(getActivity())
                            .setCallbacks(mCallbacks)
                            .build();

                    PhoneAuthProvider.verifyPhoneNumber(options);
                }
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "OTP is Sent Completed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", e.getMessage());
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(getContext(), "OTP is Sent Completed", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                codeSent = s;
                Intent intent = new Intent(getContext(), OTPActivity.class);
                intent.putExtra("otp", codeSent);
                intent.putExtra("coutryCode", countryCode);
                intent.putExtra("mobile", binding.mobileEt.getText().toString());
                startActivity(intent);

            }
        };


        binding.countrycodepicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCode = binding.countrycodepicker.getSelectedCountryCodeWithPlus();
            }
        });

        return binding.getRoot();
    }

    private String username = "", address = "", mobile = "";

    private void ValidateData() {

        username = binding.usernameEt.getText().toString().trim();
        address = binding.addressEt.getText().toString().trim();
        mobile = binding.mobileEt.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            binding.usernameEt.setError("Username Required");
        } else if (TextUtils.isEmpty(address)) {
            binding.addressEt.setError("Address Required");
        } else if (TextUtils.isEmpty(mobile)) {
            binding.mobileEt.setError("Required Contact No");
        } else if (mobile.length() < 10) {
            binding.mobileEt.setError("Invalid Number");
        } else {
            updateData();
        }
    }

    private void updateData() {

        progressDialog.setMessage("Updating user data....");
        progressDialog.show();

        DatabaseReference databaseReference = database.getReference("Users");
        long timestamp = System.currentTimeMillis();

        if (image_uri == null) {

            if (verifyedMobile.equals(binding.mobileEt.getText().toString())) {

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("username", username);
                hashMap.put("address", address);
                hashMap.put("mobile", mobile);
                hashMap.put("countryCode", countryCode);
                hashMap.put("isMobileVerified", "true");

                databaseReference.child(mAuth.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("username", username);
                hashMap.put("address", address);
                hashMap.put("mobile", mobile);
                hashMap.put("countryCode", countryCode);
                hashMap.put("isMobileVerified", "false");

                databaseReference.child(mAuth.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }


        } else {

            progressDialog.setMessage("Updating user data with profile pic....");
            progressDialog.show();

            String filepathname = "Profile_image/" + "" + timestamp;
            storageReference.getReference(filepathname).putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful()) ;
                    Uri downloadImageUri = uriTask.getResult();

                    if (uriTask.isSuccessful()) {

                        if (verifyedMobile.equals(binding.mobileEt.getText().toString())) {

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("username", username);
                            hashMap.put("address", address);
                            hashMap.put("mobile", mobile);
                            hashMap.put("countryCode", countryCode);
                            hashMap.put("isMobileVerified", "true");
                            hashMap.put("profile_pic", downloadImageUri.toString());


                            databaseReference.child(mAuth.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    } else {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("username", username);
                        hashMap.put("address", address);
                        hashMap.put("mobile", mobile);
                        hashMap.put("countryCode", countryCode);
                        hashMap.put("isMobileVerified", "false");
                        hashMap.put("profile_pic", downloadImageUri.toString());


                        databaseReference.child(mAuth.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void LoadUserData() {
        DatabaseReference databaseReference = database.getReference("Users");
        databaseReference.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username = "" + snapshot.child("username").getValue();
                String email = "" + snapshot.child("email").getValue();
                String profile_pic = "" + snapshot.child("profile_pic").getValue();
                String mobile = "" + snapshot.child("mobile").getValue();
                String address = "" + snapshot.child("address").getValue();
                String countryCode = "" + snapshot.child("countryCode").getValue();
                String isMobileVerified = "" + snapshot.child("isMobileVerified").getValue();

                if (isMobileVerified.equals("true")) {
                    verifyedMobile = "" + snapshot.child("mobile").getValue();
                } else { }


                if (countryCode.equals("" + null)) {

                } else {
                    binding.countrycodepicker.setCountryForPhoneCode(Integer.parseInt(countryCode));
                }


                binding.emailEt.setText(email);
                binding.usernameEt.setText(username);
                binding.usernameTv.setText(username);

                if (mobile.equals("" + null)) {
                    binding.mobileEt.setText("");
                } else {
                    binding.mobileEt.setText(mobile);
                }

                if (address.equals("" + null)) {
                    binding.addressEt.setText("");
                } else {
                    binding.addressEt.setText(address);
                }


                try {
                    Picasso.get().load(profile_pic).placeholder(R.drawable.man).into(binding.profileIv);
                } catch (Exception e) {
                    binding.profileIv.setImageResource(R.drawable.man);
                }

                if (snapshot.exists()) {

                    try {
                        if (isMobileVerified.equals("true")) {
                            binding.verifyMobile.setVisibility(View.GONE);
                        } else if (isMobileVerified.equals("false")) {
                            binding.verifyMobile.setVisibility(View.VISIBLE);
                        } else {
                            binding.verifyMobile.setVisibility(View.GONE);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showImagePickerDialog() {

        String[] options = {"camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Pick Image").setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    if (checkCameraPermissions()) {
                        pickFromCamera();
                    } else {
                        requestCameraPermission();
                    }
                } else {
                    if (checkStoragePermission()) {
                        pickFromGallery();

                    } else {
                        requestStoragePermission();
                    }

                }
            }
        });
        builder.show();
    }

    private void pickFromGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_Image_Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Image_Description");

        image_uri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(getActivity(), storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermissions() {

        boolean result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result && result1;

    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(getActivity(), cameraPermissions, CAMERA_REQUEST_CODE);
    }

    private void LoadLogoutDialogBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Logout");
        builder.setMessage("Do you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mAuth.signOut();
                startActivity(new Intent(getContext(), Splash_Activity.class));
                getActivity().finish();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {

                image_uri = data.getData();
                binding.profileIv.setImageURI(image_uri);

            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {

                binding.profileIv.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
        LoadVerifyedMobileNumber();
    }

    private String verifyedMobile;

    private void LoadVerifyedMobileNumber() {

        DatabaseReference databaseReference = database.getReference("Users");
        databaseReference.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}