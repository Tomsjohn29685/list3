package com.example.list3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class CalculateActivity extends AppCompatActivity {

    private TextView currency;
    private TextView show;
    private float rate=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calculate);

        currency = findViewById(R.id.currency_name);
        Intent intent = getIntent();
        String currencyName = intent.getStringExtra("currency_name");
        String rateValue = intent.getStringExtra("exchange_rate");
        currency.setText(currencyName);
        rate=Float.parseFloat(rateValue)/100;
    }

    public void onclick(View v) {
        try {
            EditText input = findViewById(R.id.inputRMB);
            String inputStr = input.getText().toString();

            if (inputStr.isEmpty()) {
                Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
                return;
            }

            float rmb = Float.parseFloat(inputStr);
            float result = rmb * rate;

            show = findViewById(R.id.showResult);
            show.setText(String.format("%.2f", result));
        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入有效的数字", Toast.LENGTH_SHORT).show();
        }
    }
}