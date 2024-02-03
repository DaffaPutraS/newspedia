package com.example.newspedia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String judul = getIntent().getStringExtra("judul");
        String kategori = getIntent().getStringExtra("kategori");
        String detail = getIntent().getStringExtra("Detail");
        String tanggal = getIntent().getStringExtra("Date");
        String poster = getIntent().getStringExtra("poster");
        TextView judulTextView = findViewById(R.id.JudulTxt);
        judulTextView.setText(judul);

        TextView kategoriTextView = findViewById(R.id.CategoryTxt);
        kategoriTextView.setText(kategori);

        TextView detailTextView = findViewById(R.id.detailTxt);
        detailTextView.setText(detail);

        TextView tanggalTextView = findViewById(R.id.tanggalTxt);
        tanggalTextView.setText(tanggal);

        ImageView posterImageView = findViewById(R.id.posterDetail);
        int drawableResourceId = getResources().getIdentifier(poster, "drawable", getPackageName());
        posterImageView.setImageResource(drawableResourceId);


    }

}