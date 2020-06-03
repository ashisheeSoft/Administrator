package com.example.adminstrator;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class HomeAd extends RecyclerView.Adapter<HomeAd.ViewHolder>{
    public Context mContext;
    public List<HomeMo> mHomeList;
    DatabaseReference reference;
    private static int counter = 0;
    private String stringVal;


    private ProgressDialog progressDialog;



    public HomeAd(Context mContext, List<HomeMo> mHomeList) {
        this.mContext = mContext;
        this.mHomeList = mHomeList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout,false);
//
//        return new HomeAd.VieHolder(view);
        View view  = LayoutInflater.from(mContext).inflate(R.layout.card_home_item,parent,false);

        progressDialog = new ProgressDialog(mContext);
        return new  HomeAd.ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        final HomeMo homemodel = mHomeList.get(position);


        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setCancelable(false);
                dialog.setTitle("Dialog on Android");
                dialog.setMessage("Are you sure you want to delete this entry?" );
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //Action for "Delete".
                        String itemLabel = String.valueOf(mHomeList.get(position));
                        mHomeList.remove(position);

                        FirebaseDatabase.getInstance().getReference().child("Sweets")
                                .child(homemodel.getItemId())
                                .removeValue();
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,mHomeList.size());
                       // Toast.makeText(mContext, itemLabel, Toast.LENGTH_SHORT).show();/
                    }
                })
                        .setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Action for "Cancel".
                            }
                        });

                final AlertDialog alert = dialog.create();
                alert.show();
                return false;
            }
        });
        holder.itemname.setText(homemodel.getItemName());
        holder.itempricekg.setText(homemodel.getItemPricekg());
        holder.itempricepcs.setText(homemodel.getItemPricepcs());

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity activity = (MainActivity) (mContext);

                CardEditDialog cardEditDialog = new CardEditDialog();
                Bundle bundle = new Bundle();
                bundle.putString("itemName",homemodel.getItemName());
                bundle.putString("itemPriceKg",homemodel.getItemPricekg());
                bundle.putString("itemPricePcs",homemodel.getItemPricepcs());
                bundle.putString("itemId",homemodel.getItemId());
                bundle.putString("itemImage",homemodel.getItemImage());
                cardEditDialog.setArguments(bundle);
                cardEditDialog.show(activity.getSupportFragmentManager(), "cardEditDialog");
                Toast.makeText(mContext, homemodel.getItemName(), Toast.LENGTH_SHORT).show();

            }
        });

        Glide.with(holder.itemimage.getContext())
                .load(homemodel.getItemImage())
                .into(holder.itemimage);

//        mHomeList.clear();


    }



    @Override
    public int getItemCount() {
        return mHomeList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemname;
        public TextView itempricekg;
        public TextView itempricepcs;
        public ImageView itemimage;
        public Button editButton;
        public LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemname = itemView.findViewById(R.id.item_name);
            itempricekg = itemView.findViewById(R.id.item_price_kg);
            itempricepcs = itemView.findViewById(R.id.item_price_pcs);
            itemimage = itemView.findViewById(R.id.item_image);

            editButton = itemView.findViewById(R.id.edit_button);

            linearLayout = itemView.findViewById(R.id.linear_layout);


        }
    }

}

