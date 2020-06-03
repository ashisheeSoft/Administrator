package com.example.adminstrator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

import static android.app.Activity.RESULT_OK;

public class CardEditDialog extends DialogFragment {
    private Uri mItemImageUri;
    private EditText editTextItemName;
    private EditText editTextPriceKg;
    private EditText editTextPricePc;

    private ImageView imageViewUploadItem;
     StorageReference mStorageReference;
    DatabaseReference reference;
    String itemId;
    String itemImage;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.card_edit_dialog,container,false);

        reference = FirebaseDatabase.getInstance().getReference().child("Sweets");
        mStorageReference = FirebaseStorage.getInstance().getReference("Sweets");


        String itemName =getArguments().getString("itemName");
        String itemPriceKg =getArguments().getString("itemPriceKg");
        String itemPricePcs =getArguments().getString("itemPricePcs");
        itemId =getArguments().getString("itemId");
         itemImage =getArguments().getString("itemImage");

        Log.d("Refrence1", reference.child(itemId).toString());


        editTextItemName = view.findViewById(R.id.et_item_name);
        editTextPriceKg = view.findViewById(R.id.et_price_kg);
        editTextPricePc = view.findViewById(R.id.et_price_pc);
        imageViewUploadItem = view.findViewById(R.id.imageView_upload_item);

        if (! itemName.equals(null) && ! itemPriceKg.equals(null) && !itemPricePcs.equals(null) && ! itemImage.equals(null)){

            Glide.with(getContext()).load(itemImage).into(imageViewUploadItem);
            editTextItemName.setText(itemName);
            editTextPriceKg.setText(itemPriceKg);
            editTextPricePc.setText(itemPricePcs);

            imageViewUploadItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CropImage.activity()
                            .setAspectRatio(1,1)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(getContext(), CardEditDialog.this);

                }
            });

        }
        Button buttonAddItem = view.findViewById(R.id.btn_edit_item);

        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), editTextItemName.getText().toString().trim(), Toast.LENGTH_SHORT).show();

                uploadItem();
            }
        });

        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mItemImageUri = result.getUri();
                imageViewUploadItem.setImageURI(mItemImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }
    private void uploadItem() {

        if (mItemImageUri != null) {
            Log.d("Card Dialog", "Inside UploadItem");
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getApplicationContext().getContentResolver(),mItemImageUri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.JPEG,25,baos);
                byte [] data = baos.toByteArray();
                mStorageReference.child(itemId).putBytes(data).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {


                        mStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String itemName = editTextItemName.getText().toString().trim();
                                String itemPriceKg = editTextPriceKg.getText().toString().trim();
                                String itemPricePc = editTextPricePc.getText().toString().trim();
                                HomeMo homeMo = new HomeMo(itemId,itemName,itemPriceKg,itemPricePc,itemImage);

                                reference.child(itemId).setValue(homeMo);

                                Toast.makeText(getContext(), "Upload Successful", Toast.LENGTH_SHORT).show();

                                getDialog().dismiss();
                            }
                        });
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }




        } else {
            Toast.makeText(getContext(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
}
