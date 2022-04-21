package com.rrmchathura.cofee_order_app.Admin.Fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.rrmchathura.cofee_order_app.R;
import com.rrmchathura.cofee_order_app.databinding.FragmentAdminAddMenuBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Admin_Add_Menu_Fragment extends Fragment {

    FragmentAdminAddMenuBinding binding;
    private ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    FirebaseStorage storageReference;

    private String coffeeName = "", coffeeQuantity = "", coffeePrice = "";
    boolean isCustomizeAvailable = false;

    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;

    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;
    private Uri image_uri;

    private String[] cameraPermissions;
    private String[] storagePermissions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdminAddMenuBinding.inflate(inflater, container, false);

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance();


        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        binding.imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickerDialog();
            }
        });

        binding.customizeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    isCustomizeAvailable = true;
                } else {
                    isCustomizeAvailable = false;
                }
            }
        });

        binding.addCoffeeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateData();
            }
        });

        return binding.getRoot();

    }

    private void ValidateData() {

        coffeeName = binding.coffeeName.getText().toString();
        coffeeQuantity = binding.coffeeQuantity.getText().toString();
        coffeePrice = binding.coffeePrice.getText().toString();

        if (TextUtils.isEmpty(coffeeName)){
            binding.coffeeName.setError("Coffee Name Required");
        }
        else if (TextUtils.isEmpty(coffeeQuantity)){
            binding.coffeeQuantity.setError("Coffee Quantity Required");
        }
        else if (TextUtils.isEmpty(coffeePrice)){
            binding.coffeePrice.setError("Coffee Price Required");
        }
        else if(image_uri == null){
            Toast.makeText(getContext(),"Please select coffee image",Toast.LENGTH_SHORT).show();
        }
        else {
            addCoffee();
        }

    }

    private void addCoffee() {

        progressDialog.setMessage("Saving Product Details with Image");
        progressDialog.show();

        String timestamp = ""+System.currentTimeMillis();

        Calendar calendar2 = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        String time = currentTime.format(calendar2.getTime());

        //upload with image
        String filepathname = "Product_images/"+ ""+ timestamp;

        DatabaseReference databaseReference = database.getReference("CoffeeMenu");

        storageReference.getReference(filepathname).putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                Uri downloadImageUri = uriTask.getResult();

                if (uriTask.isSuccessful()){

                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("coffeeId",""+timestamp);
                    hashMap.put("coffee_name",""+coffeeName);
                    hashMap.put("quantity",""+coffeeQuantity+"ml");
                    hashMap.put("price",""+coffeePrice);
                    hashMap.put("coffee_image",downloadImageUri.toString());
                    hashMap.put("isCustomizeCusAvailable",""+isCustomizeAvailable);
                    hashMap.put("timestamp",""+time);

                    databaseReference.child(timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressDialog.dismiss();
                            Toast toast = Toast.makeText(getContext(), "Coffee Added Successfully", Toast.LENGTH_SHORT);
                            toast.getView().setBackgroundTintList(getResources().getColorStateList(R.color.brown));
                            toast.show();
                            ClearData();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


    }

    private void ClearData() {
        binding.imgProduct.setImageResource(R.drawable.more);
        image_uri= null;
        binding.coffeeName.setText("");
        binding.coffeeQuantity.setText("");
        binding.coffeePrice.setText("");
        binding.customizeSwitch.setChecked(false);
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

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions((Activity) getContext(), storagePermissions, STORAGE_REQUEST_CODE);
    }

    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions((Activity) getContext(), cameraPermissions, CAMERA_REQUEST_CODE);
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

    private boolean checkCameraPermissions() {

        boolean result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result && result1;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                image_uri = data.getData();

                binding.imgProduct.setImageURI(image_uri);
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                binding.imgProduct.setImageURI(image_uri);
            }
        }
    }
}