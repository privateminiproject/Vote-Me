package com.example.voteme;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


public class ViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout root;
    public TextView txtTitle;
    public TextView txtDesc;
    public ImageView img;
    public TextView vote;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        root = itemView.findViewById(R.id.list_root);
        txtTitle = itemView.findViewById(R.id.list_title);
        txtDesc = itemView.findViewById(R.id.list_desc);
        img=itemView.findViewById(R.id.imageView);
        vote=itemView.findViewById(R.id.vote);
        root.setBackgroundResource(R.drawable.card_bg);

        Picasso.get().load(R.drawable.card_bg).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(@Nullable Palette palette) {
                        Palette.Swatch textSwatch = palette.getDarkMutedSwatch();
                        int i=textSwatch.getRgb();

                        Log.e("Color",""+i);
                    }
                });
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });



    }

    public void setVote(String string){
        vote.setText(string);
    }

    public void setTxtTitle(String string) {
        txtTitle.setText(string);
    }


    public void setTxtDesc(String string) {
        txtDesc.setText(string);
    }

    public void setImg(Uri uri){
        setImg(uri);
    }
}
