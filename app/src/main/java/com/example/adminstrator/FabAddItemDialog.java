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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import static android.app.Activity.RESULT_OK;

public class FabAddItemDialog extends AppCompatDialogFragment {

    private static final int GALLERY_PICK = 1;
    ImageView uploadItem;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_fab_dialog,null);

        uploadItem = view.findViewById(R.id.imageView_upload_item);
        uploadItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(galleryIntent, GALLERY_PICK);*/

                CropImage.activity()
                        .setAspectRatio(1,1)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(getActivity());

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
                Uri resultUri = result.getUri();
                Toast.makeText(getContext(), resultUri.toString(), Toast.LENGTH_SHORT).show();
                uploadItem.setImageURI(null);
                uploadItem.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }    }
}
