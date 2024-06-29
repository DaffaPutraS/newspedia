package com.example.newspedia.Fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.newspedia.AboutActivity;
import com.example.newspedia.ContactUsActivity;
import com.example.newspedia.FaqActivity;
import com.example.newspedia.LoginActivity;
import com.example.newspedia.MainActivity;
import com.example.newspedia.R;
import com.example.newspedia.Welcome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final long MAX_IMAGE_SIZE_BYTES = 5 * 1024 * 1024;

    private String mParam1;
    private String mParam2;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private TextView textName;
    private ImageView imageProfile;
    private static final int PICK_IMAGE_REQUEST = 1;
    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            Log.e(TAG, "User is not logged in");
            return view;
        }
        CardView cardAbout = view.findViewById(R.id.cardAbout);
        CardView cardFAQ = view.findViewById(R.id.cardFAQ);
        CardView cardContactUs = view.findViewById(R.id.cardContactUs);
        CardView cardLogout = view.findViewById(R.id.CardLogout);
        CardView cardPicture = view.findViewById(R.id.cardChangePicture);

        textName = view.findViewById(R.id.tvName);
        imageProfile = view.findViewById(R.id.ivProfile);
        displayUser();
        cardLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), Welcome.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        cardAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent untuk membuka AboutActivity
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
            }
        });

        cardFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent untuk membuka FAQActivity
                Intent intent = new Intent(getActivity(), FaqActivity.class);
                startActivity(intent);
            }
        });

        cardContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent untuk membuka ContactUsActivity
                Intent intent = new Intent(getActivity(), ContactUsActivity.class);
                startActivity(intent);
            }
        });
        cardPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent untuk membuka ChangePictureActivity

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri photoUri = data.getData();

            // Lakukan pengunggahan foto ke Firebase Storage dan pembaruan URL foto profil
            uploadPhotoAndUpdateProfile(photoUri);
        }
    }
    private void uploadPhotoAndUpdateProfile(Uri photoUri) {

        try {
            // Periksa ukuran gambar sebelum mengunggahnya
            File imageFile = new File(photoUri.getPath());
            long imageSize = imageFile.length();
            if (imageSize > MAX_IMAGE_SIZE_BYTES) {
                // Gambar terlalu besar, berikan pesan kesalahan atau tindakan yang sesuai
                Toast.makeText(getActivity(), "Ukuran gambar terlalu besar", Toast.LENGTH_SHORT).show();

                return;

            }

            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = storageRef.child("profile_images/" + firebaseUser.getUid() + ".jpg");
            UploadTask uploadTask = imageRef.putFile(photoUri);

            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@org.checkerframework.checker.nullness.qual.NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        // Foto berhasil diunggah ke Firebase Storage
                        // Dapatkan URL gambar baru
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Update URL foto profil pengguna di Firebase Authentication
                                UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                        .setPhotoUri(uri)
                                        .build();

                                firebaseUser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@org.checkerframework.checker.nullness.qual.NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {


                                            // Foto profil berhasil diperbarui
                                            // Tampilkan foto baru menggunakan Glide atau cara yang sama seperti sebelumnya
                                            Glide.with(getActivity())
                                                    .load(uri)
                                                    .circleCrop()
                                                    .into(imageProfile);
                                            reload();

                                        } else {
                                            Toast.makeText(getActivity(), "Gambar Tidak tersimpan", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    } else {
                        // Gagal mengunggah foto ke Firebase Storage
                        // Tampilkan pesan error atau lakukan penanganan kesalahan lainnya
                        Toast.makeText(getActivity(), "Gambar tida tersimpan", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void reload() {
        Toast.makeText(getActivity(), "Berhasil Mengubah Photo Profile", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void displayUser() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            return;
        }

        String displayName = firebaseUser.getDisplayName();
        if (displayName != null) {
            textName.setText( displayName);
        } else {
            textName.setText("User");
        }

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String username = snapshot.child("username").getValue(String.class);
                    if (username != null && !username.isEmpty()) {
                        textName.setText(username);
                    } else {
                        textName.setText("No username found");
                    }
                } else {
                    textName.setText("Snapshot does not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "DatabaseError: " + error.getMessage());
            }
        });

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("profile_images/" + firebaseUser.getUid() + ".jpg");
        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            if (uri != null) {
                Glide.with(this)
                        .load(uri)
                        .into(imageProfile);
            } else {
                imageProfile.setImageResource(R.drawable.logo);
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Failed to load profile image: " + e.getMessage());
            imageProfile.setImageResource(R.drawable.logo);
        });
    }
}
