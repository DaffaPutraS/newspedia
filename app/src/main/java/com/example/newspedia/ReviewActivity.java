package com.example.newspedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReviewActivity extends AppCompatActivity {

    private CardView cardArrowLeftFAQ;
    private EditText inputReviewName, inputDescriptionReview;
    private View sendReviewBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        cardArrowLeftFAQ = findViewById(R.id.cardArrowLeftFAQ);
        inputReviewName = findViewById(R.id.inputReviewName);
        inputDescriptionReview = findViewById(R.id.inputDescriptionReview);
        sendReviewBtn = findViewById(R.id.sendReviewBtn);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Review");

        // Get current user
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            inputReviewName.setText(currentUser.getDisplayName());
        }

        cardArrowLeftFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        sendReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReview();
            }
        });
    }

    private void submitReview() {
        String reviewName = inputReviewName.getText().toString().trim();
        String description = inputDescriptionReview.getText().toString().trim();

        if (TextUtils.isEmpty(description)) {
            inputDescriptionReview.setError("Description is required");
            return;
        }

        String reviewId = mDatabase.push().getKey();
        Review review = new Review(reviewName, description);

        if (reviewId != null) {
            mDatabase.child(reviewId).setValue(review).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(ReviewActivity.this, "Review submitted", Toast.LENGTH_SHORT).show();
                    inputDescriptionReview.setText("");
                } else {
                    Toast.makeText(ReviewActivity.this, "Failed to submit review", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static class Review {
        public String reviewName;
        public String description;

        public Review() {
            // Default constructor required for calls to DataSnapshot.getValue(Review.class)
        }

        public Review(String reviewName, String description) {
            this.reviewName = reviewName;
            this.description = description;
        }
    }
}
