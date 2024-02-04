package com.example.newspedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

public class FaqActivity extends AppCompatActivity {

    CardView cardArrowLeftFAQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        cardArrowLeftFAQ = findViewById(R.id.cardArrowLeftFAQ);

        cardArrowLeftFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}