package com.example.civiladvocacyapp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivityViewHolder extends RecyclerView.ViewHolder {

    TextView txtOfficeName, txtOfficialNameAndParty;
    public MainActivityViewHolder(@NonNull View itemView) {
        super(itemView);
        this.txtOfficeName = itemView.findViewById(R.id.txtOfficeName);
        this.txtOfficialNameAndParty = itemView.findViewById(R.id.txtOfficialNameAndParty);
    }
}
