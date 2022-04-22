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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.rrmchathura.cofee_order_app.R;
import com.rrmchathura.cofee_order_app.databinding.FragmentCoffeeEditPopupBinding;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import fr.tvbarthel.lib.blurdialogfragment.BlurDialogFragment;
import fr.tvbarthel.lib.blurdialogfragment.SupportBlurDialogFragment;

public class Coffee_Edit_Popup_Fragment extends SupportBlurDialogFragment {

    FragmentCoffeeEditPopupBinding binding;
    private String coffeeImage,coffeeName,coffeePrice,isCustomizeAvailable,Quantity,coffeeId;
    private String quantityEt,coffeeNameEt,coffeePriceEt;

    FirebaseDatabase database;
    FirebaseStorage storageReference;

    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;

    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;
    private Uri image_uri;
    private boolean checkCustomizeAvailable = false;

    private String[] cameraPermissions;
    private String[] storagePermissions;

    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCoffeeEditPopupBinding.inflate(inflater, container, false);

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        database = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance();

        Bundle bundle = this.getArguments();

        if(bundle != null) {

            coffeeImage = bundle.getString("coffeeImage");
            coffeeName = bundle.getString("coffeeName");
            coffeePrice = bundle.getString("coffeePrice");
            isCustomizeAvailable = bundle.getString("isCustomizeAvailable");
            Quantity = bundle.getString("Quantity");
            coffeeId = bundle.getString("coffeeId");

            try {
                Picasso.get().load(coffeeImage).placeholder(R.drawable.spinner).into(binding.imgProduct);

            } catch (Exception e) {
                binding.imgProduct.setImageResource(R.drawable.spinner);
            }

            binding.coffeeName.setText(coffeeName);
            binding.coffeeQuantity.setText(Quantity+" ml");
            binding.coffeePrice.setText(coffeePrice);

            if (isCustomizeAvailable.equals("true")){
                binding.customizeSwitch.setChecked(true);
            }
            else {
                binding.customizeSwitch.setChecked(false);
            }

        }

        binding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

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
                    checkCustomizeAvailable = true;
                } else {
                    checkCustomizeAvailable = false;
                }
            }
        });

        binding.updateCoffeeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateData();
            }
        });


        return binding.getRoot();
    }

    private void ValidateData() {

        coffeeNameEt = binding.coffeeName.getText().toString();
        quantityEt = binding.coffeeQuantity.getText().toString().replace("ml","");
        coffeePriceEt = binding.coffeePrice.getText().toString();

        if (TextUtils.isEmpty(coffeeName)){
            binding.coffeeName.setError("Coffee Name Required");
        }
        else if (TextUtils.isEmpty(quantityEt)){
            binding.coffeeQuantity.setError("Coffee Quantity Required");
        }
        else if (TextUtils.isEmpty(coffeePriceEt)){
            binding.coffeePrice.setError("Coffee Price Required");
        }
        else {
            UpdateCoffee();
        }
    }

    private void UpdateCoffee() {

        progressDialog.setMessage("Coffee Updating");
        progressDialog.show();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("CoffeeMenu");

        if (image_uri == null){

            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("coffee_name",""+coffeeNameEt);
            hashMap.put("quantity",""+quantityEt);
            hashMap.put("price",""+coffeePriceEt);
            hashMap.put("isCustomizeCusAvailable",""+checkCustomizeAvailable);

            databaseReference.child(coffeeId).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(),"Updated",Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        }else {

            progressDialog.setMessage("Updating Coffee Details with Image");
            progressDialog.show();

            String timestamp = ""+System.currentTimeMillis();

            //upload with image
            String filepathname = "Product_images/"+ ""+ timestamp;

            storageReference.getReference(filepathname).putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful());
                    Uri downloadImageUri = uriTask.getResult();

                    if (uriTask.isSuccessful()){

                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("coffee_name",""+coffeeNameEt);
                        hashMap.put("quantity",""+quantityEt);
                        hashMap.put("price",""+coffeePriceEt);
                        hashMap.put("coffee_image",downloadImageUri.toString());
                        hashMap.put("isCustomizeCusAvailable",""+checkCustomizeAvailable);

                        databaseReference.child(coffeeId).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.dismiss();
                                Toast toast = Toast.makeText(getContext(), "Coffee Updated Successfully", Toast.LENGTH_SHORT);
                                toast.getView().setBackgroundTintList(getResources().getColorStateList(R.color.brown));
                                toast.show();


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