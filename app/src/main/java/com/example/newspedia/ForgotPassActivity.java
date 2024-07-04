package com.example.newspedia;

import android.os.Bundle;
import android.webkit.WebView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ForgotPassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        WebView webView = findViewById(R.id.webview);
        ConstraintLayout backButtonforgot = findViewById(R.id.backForgotPassword);
        webView.getSettings().setJavaScriptEnabled(true); // Enable JavaScript if required by the site
        webView.loadUrl("https://newspedia-1d8af.web.app/app/src/main/forgot-password.html");

        backButtonforgot.setOnClickListener(view -> {
            finish();
        });

    }
}