package com.example.civiladvocacyapp;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityViewHolder> {

    private static final String TAG = "MainActivityAdapter";
    private final MainActivity mainActivity;
    private final CivicInformation civicInformation;
    MainActivityAdapter(MainActivity mainActivity, CivicInformation civicInformation)
    {
        this.mainActivity = mainActivity;
        this.civicInformation = civicInformation;
    }
    @NonNull
    @Override
    public MainActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_main_row, parent, false);
        itemView.setOnClickListener(mainActivity);
        itemView.setOnLongClickListener(mainActivity);

        return new MainActivityViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MainActivityViewHolder holder, int position) {
        try {
            Offices offices = civicInformation.getOffices().get(position);
            holder.txtOfficeName.setText(offices.getOffices_name());
            for(int i=0; i<offices.getOffices_officialIndices().size();i++) {
                Officials officials = civicInformation.getOfficials().get(offices.getOffices_officialIndices().get(i));
                holder.txtOfficialNameAndParty.setText(officials.getOfficials_name() + " (" + officials.getOfficials_party() + ") ");
            }
        }
        catch (Exception e){
            Log.e(TAG, "onBindViewHolder: ", e);
        }
    }

    @Override
    public int getItemCount() {
        try{
            return civicInformation.getOffices().size();
        }
        catch (Exception e) {
            Log.e(TAG, "getItemCount: ", e);
            return 0;
        }
    }
}
