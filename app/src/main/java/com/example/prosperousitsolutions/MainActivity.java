package com.example.prosperousitsolutions;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {
//    RadioGroup radioGroup;
//    RadioButton search, offer;

    protected void init(){
//        search = findViewById(R.id.search);
//        offer = findViewById(R.id.offer);
//        radioGroup = findViewById(R.id.toggle);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                if (i == R.id.search) {
//                    search.setTextColor(Color.parseColor("#FFFFFF"));
//                    offer.setTextColor(Color.parseColor("#4FC3F7"));
//                    search.setTextSize(20);
//                    offer.setTextSize(10);
//                }
//                else if (i == R.id.offer){
//                    offer.setTextColor(Color.parseColor("#FFFFFF"));
//                    search.setTextColor(Color.parseColor("#4FC3F7"));
//                    offer.setTextSize(20);
//                    search.setTextSize(10);
//                }
//            }
//        });

    }


}