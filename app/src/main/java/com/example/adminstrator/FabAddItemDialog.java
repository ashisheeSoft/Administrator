package com.example.adminstrator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import static android.app.Activity.RESULT_OK;

public class FabAddItemDialog extends AppCompatDialogFragment {

    private static final int GALLERY_PICK = 1;
    ImageView imageViewUploadItem;

    private Uri mItemImageUri;

    private StorageReference mStorageReference;
    private DatabaseReference mDatabaseRefrence;

    Button buttonAddItem;
    EditText editTextItemName;
    EditText editTextPriceKg;
    EditText editTextPricePc;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_fab_dialog,null);

        editTextItemName = view.findViewById(R.id.et_item_name);
        editTextPriceKg = view.findViewById(R.id.et_price_kg);
        editTextPricePc = view.findViewById(R.id.et_price_pc);

        buttonAddItem = view.findViewById(R.id.btn_add_item);

        mStorageReference = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRefrence = FirebaseDatabase.getInstance().getReference("uploads");

        imageViewUploadItem = view.findViewById(R.id.imageView_upload_item);
        imageViewUploadItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent galleryIntent = new Intent();
//                galleryIntent.setType("image/*");
//                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
//
//                startActivityForResult(galleryIntent, GALLERY_PICK);

                CropImage.activity()
                        .setAspectRatio(1,1)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(getContext(), FabAddItemDialog.this);

            }
        });

        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(), mItemImageUri.toString(), Toast.LENGTH_SHORT).show();
                uploadItem();
            }
        });

                builder.setView(view);
        return builder.create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mItemImageUri = result.getUri();
//                Toast.makeText(getContext(), resultUri.toString(), Toast.LENGTH_SHORT).show();
                imageViewUploadItem.setImageURI(mItemImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }
    private void uploadItem() {
        if (mItemImageUri != null) {
            StorageReference itemRefrence = mStorageReference.child(System.currentTimeMillis() + "");
            itemRefrence.putFile(mItemImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "Upload Successful", Toast.LENGTH_SHORT).show();
                    String itemName = editTextItemName.getText().toString();
                    String itemPriceKg = editTextPriceKg.getText().toString();
                    String itemPricePc = editTextPricePc.getText().toString();

                    HomeMo upload = new HomeMo(taskSnapshot.getUploadSessionUri().toString(),itemName,itemPriceKg, itemPricePc, "12346");
                    String uploadId = mDatabaseRefrence.push().getKey();
                    mDatabaseRefrence.child(uploadId).setValue(upload);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                }
            });
        } else {
            Toast.makeText(getContext(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
}
