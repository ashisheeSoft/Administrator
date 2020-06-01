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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Random;

import static android.app.Activity.RESULT_OK;

public class FabAddItemDialog extends AppCompatDialogFragment {

    private static final int GALLERY_PICK = 1;
    private ImageView imageViewUploadItem;

    private Uri mItemImageUri;

    private StorageReference mStorageReference;
    private DatabaseReference mDatabaseRefrence;

    private EditText editTextItemName;
    private EditText editTextPriceKg;
    private EditText editTextPricePc;

    private String itemName;
    private String itemPriceKg;
    private String itemPricePc;
    private String itemImage;


    String num;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_fab_dialog,null);

        editTextItemName = view.findViewById(R.id.et_item_name);
        editTextPriceKg = view.findViewById(R.id.et_price_kg);
        editTextPricePc = view.findViewById(R.id.et_price_pc);

        Button buttonAddItem = view.findViewById(R.id.btn_add_item);
        Random rnd = new Random();
      int  orderidint = 100000 + rnd.nextInt(900000);
      num = String.valueOf(orderidint);

        mStorageReference = FirebaseStorage.getInstance().getReference("Sweets").child(num);
        mDatabaseRefrence = FirebaseDatabase.getInstance().getReference("Sweets").child(num);

        imageViewUploadItem = view.findViewById(R.id.imageView_upload_item);
        imageViewUploadItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity()
                        .setAspectRatio(1,1)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(getContext(), FabAddItemDialog.this);

            }
        });

        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                imageViewUploadItem.setImageURI(mItemImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }
    private void uploadItem() {
        if (mItemImageUri != null) {

            mStorageReference.putFile(mItemImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {


                    mStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            itemImage = uri.toString();
                            itemName = editTextItemName.getText().toString();
                            itemPriceKg = editTextPriceKg.getText().toString();
                            itemPricePc = editTextPricePc.getText().toString();

                             HomeMo upload = new HomeMo(itemImage, itemName, itemPriceKg, itemPricePc, num);

                            mDatabaseRefrence.setValue(upload);

                            Toast.makeText(getContext(), "Upload Successful", Toast.LENGTH_SHORT).show();

                            getDialog().dismiss();
                        }
                    });
                }
            });



        } else {
            Toast.makeText(getContext(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
}
