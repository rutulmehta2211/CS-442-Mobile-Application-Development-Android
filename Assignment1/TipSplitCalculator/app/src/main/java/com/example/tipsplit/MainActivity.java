package com.example.tipsplit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "GoButton";
    private EditText Totalbill;
    private EditText TipAmount;
    private EditText TotalWithTip;
    private EditText NumberOfPeople;
    private EditText TotalPerPerson;
    private EditText Overage;
    private RadioGroup TipPercent;
    double bill;
    int tipPercent = -1;
    double tip;
    double total;
    int person;
    double perperson;
    double overage;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Totalbill = findViewById(R.id.txtTotalBill);
        TipAmount = findViewById(R.id.txtTipAmount);
        TotalWithTip = findViewById(R.id.txtTotalWithTip);
        NumberOfPeople = findViewById(R.id.txtNumberOfPeople);
        TotalPerPerson = findViewById(R.id.txtTotalperPerson);
        Overage = findViewById(R.id.txtOverage);
        TipPercent = findViewById(R.id.radGrpTipPercent);

        Totalbill.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Empty Method
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!Totalbill.getText().toString().isEmpty() && tipPercent != -1){
                    bill = Double.parseDouble(Totalbill.getText().toString());
                    tip = (bill*tipPercent)/100;
                    total = bill + tip;
                    TipAmount.setText("$"+df.format(tip));
                    TotalWithTip.setText("$"+df.format(total));

                    CalculateOverage();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Empty Method
            }
        });

    }

    public void doRadioButtonClicked(View v){
        if(!Totalbill.getText().toString().isEmpty()){
            bill = Double.parseDouble(Totalbill.getText().toString());
            switch (v.getId()){
                case R.id.radBtn12:
                    tipPercent=12;
                    break;
                case R.id.radBtn15:
                    tipPercent=15;
                    break;
                case R.id.radBtn18:
                    tipPercent=18;
                    break;
                case R.id.radBtn20:
                    tipPercent=20;
                    break;
                default:
                    tipPercent=-1;
            }
            tip = (bill*tipPercent)/100;
            total = bill + tip;
            TipAmount.setText("$"+df.format(tip));
            TotalWithTip.setText("$"+df.format(total));

            CalculateOverage();
        }
    }

    public void doGoButtonClicked(View v){
        CalculateOverage();
    }

    public void doClearButtonClicked(View v){
        Totalbill.getText().clear();
        TipPercent.clearCheck();
        TipAmount.getText().clear();
        TotalWithTip.getText().clear();
        NumberOfPeople.getText().clear();
        TotalPerPerson.getText().clear();
        Overage.getText().clear();
        tipPercent=-1;
    }

    private void CalculateOverage()
    {
        try {
            if (!NumberOfPeople.getText().toString().isEmpty() && !NumberOfPeople.getText().toString().equals("0")) {
                person = Integer.parseInt(NumberOfPeople.getText().toString());
                perperson = total / person;
                BigDecimal bdUp_perperson = new BigDecimal(perperson).setScale(2, RoundingMode.UP);
                TotalPerPerson.setText("$" + df.format(bdUp_perperson.doubleValue()));
                double temp_total1 = Double.parseDouble(df.format(bdUp_perperson.doubleValue())) * person;
                double temp_total2 = Double.parseDouble(df.format(total));
                overage = temp_total1 - temp_total2;
                Overage.setText("$" + df.format(overage));
            }
        }
        catch (Exception e){
            Toast.makeText(this,"Something Went Wrong",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("TOTALBILL",Totalbill.getText().toString());
        outState.putString("TIPAMOUNT",TipAmount.getText().toString());
        outState.putString("TOTALWITHTIP",TotalWithTip.getText().toString());
        outState.putString("NUMBAROFPEOPLE",NumberOfPeople.getText().toString());
        outState.putString("TOTALPERPERSON",TotalPerPerson.getText().toString());
        outState.putString("OVERAGE",Overage.getText().toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Totalbill.setText(savedInstanceState.getString("TOTALBILL"));
        TipAmount.setText(savedInstanceState.getString("TIPAMOUNT"));
        TotalWithTip.setText(savedInstanceState.getString("TOTALWITHTIP"));
        NumberOfPeople.setText(savedInstanceState.getString("NUMBAROFPEOPLE"));
        TotalPerPerson.setText(savedInstanceState.getString("TOTALPERPERSON"));
        Overage.setText(savedInstanceState.getString("OVERAGE"));
    }
}