package com.example.newspedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

public class ContactUsActivity extends AppCompatActivity {

    CardView cardArrowLeftContactUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        cardArrowLeftContactUs = findViewById(R.id.cardArrowLeftContactUs);

        cardArrowLeftContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}