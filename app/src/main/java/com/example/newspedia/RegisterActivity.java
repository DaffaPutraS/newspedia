package com.example.newspedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newspedia.modelItem.UserDomain;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class RegisterActivity extends AppCompatActivity {
    private EditText emailTxt,passTxt,usernameTxt;
    private TextView signInTxt;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;

    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        Interaction();
    }

    private void Interaction() {
        signInTxt.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
        });
        registerBtn.setOnClickListener(view -> {
            if(usernameTxt.getText().length()>0 && emailTxt.getText().length()>0 && passTxt.getText().length()>0){
                register(usernameTxt.getText().toString(),emailTxt.getText().toString(),passTxt.getText().toString());
//                startActivity(new Intent(this, LoginActivity.class));
//                Toast.makeText(this, "User dibuat", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Please Insert All Of Them", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void register(String username, String email, String password) {
        Drawable defaultIcon = getResources().getDrawable(R.drawable.minilogo);
        Bitmap bitmap = ((BitmapDrawable) defaultIcon).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if(task.isSuccessful() && mAuth.getCurrentUser() != null){
                FirebaseUser firebaseUser = task.getResult().getUser();
                if (firebaseUser != null) {
                    StorageReference imageRef = storageRef.child("profile_images/"+firebaseUser.getUid()+ ".jpg");

                    DatabaseReference userRef = firebaseDatabase.getReference("users").child(firebaseUser.getUid());
                    UserDomain user = new UserDomain();
                    user.setUserId(firebaseUser.getUid());
                    user.setUsername(username);
                    user.setEmail(email);
                    UploadTask uploadTask = imageRef.putBytes(data);
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(username)
                            .setPhotoUri(firebaseUser.getPhotoUrl())
                            .build();
                    firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            userRef.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        reload();
                                    }else {
                                        Toast.makeText(getApplicationContext(), "Gagal Menyimpan ke database", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });


                        }
                    });
                }


            }else{
                Toast.makeText(this, "Failed to create user", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void reload() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }
    private void initView(){
        signInTxt = findViewById(R.id.goSignIn);
        usernameTxt = findViewById(R.id.userInputTxt);
        emailTxt  = findViewById(R.id.emailRegisterInputText);
        passTxt = findViewById(R.id.passRegisterInputText);
        registerBtn = findViewById(R.id.buttonRegisterSubmit);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
    }
}