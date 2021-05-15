package com.android.artrial2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.artrial2.entities.ObjectFile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {
    private final int PICK_IMAGE_REQUEST = 22;
    private Button selectImage, selectGLB, updateDatabase, updateImage, updateGLB;
    private EditText heritageName;
    private MKLoader adminLoader;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private Uri filePath;
    private TextInputLayout heritageLayout;
    private ArrayList<String> imageNameList = new ArrayList<>();
    private ArrayList<String> imageUrlList = new ArrayList<>();
    private String currentImageUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        findViewById(R.id.adminHeritageNameTextField);
        heritageName = findViewById(R.id.adminHeritageName);
        selectImage = findViewById(R.id.adminSelectImage);
        selectGLB = findViewById(R.id.adminSelectGLB);
        updateDatabase = findViewById(R.id.adminUpdateDatabase);
        updateImage = findViewById(R.id.adminUploadImage);
        updateGLB = findViewById(R.id.adminUploadGLB);
        adminLoader = findViewById(R.id.adminLoader);
        heritageLayout = findViewById(R.id.adminHeritageNameTextField);
        firebaseStorage = FirebaseStorage.getInstance();
        heritageLayout.setErrorIconDrawable(null);
        AdminActivity.FireabaseAsyncCard fireabaseAsyncCard = new AdminActivity.FireabaseAsyncCard();
        fireabaseAsyncCard.execute();
        selectImage.setOnClickListener(view -> selectFile("image"));
        selectGLB.setOnClickListener(view -> selectFile("glb"));

        updateImage.setOnClickListener(view -> {
            if((heritageName.getText().toString() != null) && (!heritageName.getText().toString().isEmpty())){
                String finalName = "";
                String[] completeName = heritageName.getText().toString().split(" ");
                for(String a: completeName){
                    finalName += a;
                }
                finalName = finalName.toLowerCase();
                updateStorage("images/", "image", finalName);
            }else{
                heritageLayout.setError("Enter heritage name");
            }

        });
        updateGLB.setOnClickListener(view -> {
            if((heritageName.getText().toString() != null) && (!heritageName.getText().toString().isEmpty())){
                String finalName = "";
                String[] completeName = heritageName.getText().toString().split(" ");
                for(String a: completeName){
                    finalName += a;
                }
                finalName = finalName.toLowerCase();
                updateStorage("models/", "glb", finalName);
            }else{
                heritageLayout.setError("Enter heritage name");
            }

        });
        updateDatabase.setOnClickListener(view -> {
            if((currentImageUrl != null) && (!currentImageUrl.isEmpty())){
                updateDatabase(heritageName.getText().toString(), currentImageUrl);
            }else{
                Toast.makeText(AdminActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void updateDatabase(String imageName, String imageUrl) {
        adminLoader.setVisibility(View.VISIBLE);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference updateRef = firebaseDatabase.getReference("lUFMH5URcVPxCfSVKNRJopdXoy23");

        imageNameList.add(imageName);
        imageUrlList.add(imageUrl);

        ObjectFile objectFile = new ObjectFile(imageNameList, imageUrlList);

        updateRef.setValue(objectFile);
        adminLoader.setVisibility(View.INVISIBLE);
        Log.d("ABCDE", "this is updated image list:" + imageNameList);
        Log.d("ABCDE", "this is updated url list:" + imageUrlList);
    }
    private void fetchImageUrl(){

    }
    private class FirebaseFetchUrl extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            adminLoader.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            Log.d("ABCDEF", "this is post execute " + s);
        }


        @Override
        protected Boolean doInBackground(Void... voids) {
            String finalName = "";
            String[] completeName = heritageName.getText().toString().split(" ");
            for(String a: completeName){
                finalName += a;
            }
            finalName = finalName.toLowerCase();
            storageReference = firebaseStorage.getReference("images/"+finalName);
            storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                Toast.makeText(AdminActivity.this, "Database updated successfully.", Toast.LENGTH_SHORT).show();
                currentImageUrl = uri.toString();
            })
                    .addOnFailureListener(e -> {
                Toast.makeText(AdminActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
            return true;
        }
    }

    private void selectFile(String fileType) {
        Intent intent = new Intent();
        if (fileType.equals("image")) {
            intent.setType("image/*");
        } else {
            intent.setType("*/*");
        }
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File from here..."), PICK_IMAGE_REQUEST);
    }

    private class FireabaseAsyncCard extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            adminLoader.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            Log.d("ABCDEF", "this is post execute " + s);
        }


        @Override
        protected Boolean doInBackground(Void... voids) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference arModelReference = firebaseDatabase.getReference("lUFMH5URcVPxCfSVKNRJopdXoy23");
            arModelReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    imageNameList = new ArrayList<>();
                    Map completeMap = (Map) snapshot.getValue();
                    imageNameList = (ArrayList<String>) completeMap.get("image_name");
                    imageUrlList = (ArrayList<String>) completeMap.get("model_name");
                    adminLoader.setVisibility(View.INVISIBLE);
//                    setUpCards(imageNameList, imageUrlList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    adminLoader.setVisibility(View.INVISIBLE);
                    Log.d("ABCDE", "this database error:" + error.getDetails());
                    Toast.makeText(AdminActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
            Log.d("ABCDEF", "trying something...");
            return true;
        }
    }

    private void updateStorage(String databaseLocation, String fileType, String fileName) {
        storageReference = firebaseStorage.getReference(databaseLocation);
        if (filePath != null) {
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference ref;
            if (fileType.equals("image")) {
                ref = storageReference.child(fileName);
            } else {
                ref = storageReference.child(fileName + ".glb");
            }

            ref.putFile(filePath)
                    .addOnSuccessListener(
                            taskSnapshot -> {
                                AdminActivity.FirebaseFetchUrl firebaseFetchUrl = new AdminActivity.FirebaseFetchUrl();
                                filePath = null;
                                firebaseFetchUrl.execute();
                                progressDialog.dismiss();
                                Toast.makeText(AdminActivity.this, "File Uploaded!!", Toast.LENGTH_SHORT).show();
                            })

                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(AdminActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("ABCDE", e.getLocalizedMessage());
                    })
                    .addOnProgressListener(
                            taskSnapshot -> {
                                double progress
                                        = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                progressDialog.setMessage("Uploaded " + (int) progress + "%");
                            });

        }
        else{
            Toast.makeText(this, "Select file first.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

        }
    }


}