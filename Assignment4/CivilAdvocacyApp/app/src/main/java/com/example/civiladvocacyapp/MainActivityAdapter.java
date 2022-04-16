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

        return new MainActivityViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MainActivityViewHolder holder, int position) {
        try {
            Officials officials = civicInformation.getOfficials().get(position);
            holder.txtOfficialNameAndParty.setText(officials.getOfficials_name() + " (" + officials.getOfficials_party() + ") ");
            for(int i=0;i<civicInformation.getOffices().size();i++) {
                Offices offices = civicInformation.getOffices().get(i);
                if(offices.getOffices_officialIndices().contains(position)){
                    holder.txtOfficeName.setText(offices.getOffices_name());
                    break;
                }
            }
        }
        catch (Exception e){
            Log.e(TAG, "onBindViewHolder: ", e);
        }
    }

    @Override
    public int getItemCount() {
        try{
                Log.d(TAG, "getItemCount: "+civicInformation.getOfficials().size());
                return civicInformation.getOfficials().size();
        }
        catch (Exception e) {
            Log.e(TAG, "getItemCount: ", e);
        }
        return 0;
    }
}
