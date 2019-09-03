package com.example.voteme;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



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
