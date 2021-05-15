package com.android.artrial2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.artrial2.entities.ObjectFile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private final int PICK_IMAGE_REQUEST = 22;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private EditText getHeritageName;
    private EditText fileName;
    private TextView fileNameText;
    private Button startARScene;
    private Button selectImage;
    private Button uploadImage;
    private Button selectGlb;
    private Button uploadGlb;
    private Button fetchDatabase;
    private Button updateDatabase;
    //    private FirebaseAuth firebaseAuth;
    private String imageNameString;
    private ArrayList<String> imageUrl = new ArrayList<>();
    private Uri filePath;
    private boolean isUrlFetched = false;
    private ObjectFile obj;
    private ArrayList<String> imageNameList = new ArrayList<>();
    private ArrayList<String> modelNameList = new ArrayList<>();

    /* private void firebaseUser(String email, String password) {
          firebaseAuth.createUserWithEmailAndPassword(email, password)
                  .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                      @Override
                      public void onComplete(@NonNull Task<AuthResult> task) {
                          if (task.isSuccessful()) {

                              Toast.makeText(MainActivity.this, "Registration successfully", Toast.LENGTH_SHORT).show();
                          } else {
                              Toast.makeText(MainActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                          }
                      }
                  })
                  .addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {
                          if (e.getLocalizedMessage().contains("email address already in use")) {
                              Toast.makeText(MainActivity.this, "Account already created", Toast.LENGTH_SHORT).show();
                              Toast.makeText(MainActivity.this, "Signing In...", Toast.LENGTH_SHORT).show();
                              firebaseAuth.signInWithEmailAndPassword(email, password).
                                      addOnCompleteListener(
                                              new OnCompleteListener<AuthResult>() {
                                                  @Override
                                                  public void onComplete(@NonNull Task<AuthResult> task) {
                                                      if (task.isSuccessful()) {
                                                          Toast.makeText(MainActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                                                      } else {
                                                          Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                                                      }
                                                  }
                                              }
                                      )
                                      .addOnFailureListener(new OnFailureListener() {
                                          @Override
                                          public void onFailure(@NonNull Exception e) {
                                              Toast.makeText(MainActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();

                                          }
                                      });
                          }
                      }
                  });
      }
  */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseStorage = FirebaseStorage.getInstance();
//        firebaseAuth = FirebaseAuth.getInstance();
        /*if (firebaseAuth.getInstance().getCurrentUser() == null) {
            String email = "abcde@gmail.com";
            String password = "MiniProject123";
            firebaseUser(email, password);
        }*/
        startARScene = findViewById(R.id.btnGetHeritageName);
        getHeritageName = findViewById(R.id.heritageName);
        setUpView();

        startARScene.setOnClickListener(view -> {
            String heritageName = getHeritageName.getText().toString().trim();
            if (heritageName != null) {
                Intent intent = new Intent(MainActivity.this, ArSceneView.class);
                intent.putExtra("heritageName", heritageName);
                startActivity(intent);

            } else {
                Toast.makeText(MainActivity.this, "Enter heritage name", Toast.LENGTH_SHORT).show();
            }

        });


        fetchDatabase.setOnClickListener(view -> {
            fetchUserData();
//            startActivity(new Intent(this, HomeActivity.class));
        });
        updateDatabase.setOnClickListener(view -> {
                    fileNameText.setText("File name: " + fileName.getText().toString().trim());
                    updateDatabase();
                }
        );
        selectImage.setOnClickListener(
                view -> {
                    if (isUrlFetched)
                        convertList();
                    else
                        modifyData();
                }
//                        selectFile("image")
        );


        uploadImage.setOnClickListener(view -> {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            /*String fileNameText = null;
            fileNameText = fileName.getText().toString().trim();
            if (fileNameText != null) {
                updateStorage("images/", "image");
            } else {
                Toast.makeText(MainActivity.this, "Enter image name", Toast.LENGTH_SHORT).show();
            }*/
        });
        selectGlb.setOnClickListener(
                view -> selectFile("glb")
        );
        uploadGlb.setOnClickListener(view -> {
            String fileNameText = null;
            fileNameText = fileName.getText().toString().trim();
            if (fileNameText != null) {
                updateStorage("models/", "glb");
            } else {
                Toast.makeText(MainActivity.this, "Enter image name", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void convertList() {

        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("key2", imageNameList);
        intent.putExtra("key3", imageUrl);
        startActivity(intent);
    }

    private void modifyData() {
        Log.d("ABCDE", "image name  -> " + imageNameList);
//        Log.d("ABCDE", "image urls -> " + imageNameList);

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        for (int i = 0; i < imageNameList.size(); i++) {
            StorageReference reference = firebaseStorage.getReference("images/" + imageNameList.get(i) + ".jpg");
            reference.getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        String image = uri.toString();
                        Log.d("ABCDE", "image url-> " + image);
                        isUrlFetched = true;
                        Log.d("ABCDE", "image urls -> " + imageUrl);
                        imageUrl.add(uri.toString());
                    })
                    .addOnFailureListener(e -> {
                        Log.d("ABCDE", "error msg: " + e.getLocalizedMessage());
                        Toast.makeText(MainActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                    });
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

    private void fetchUserData() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference arModelReference = firebaseDatabase.getReference("lUFMH5URcVPxCfSVKNRJopdXoy23");
        ObjectFile objectFile;
        arModelReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap object = (HashMap) snapshot.getValue();
                imageNameList = new ArrayList<>();
                modelNameList = new ArrayList<>();
                imageNameList.addAll((Collection<? extends String>) object.get("image_name"));
                modelNameList.addAll((Collection<? extends String>) object.get("model_name"));
                Log.d("ABCDE", "this is fetch image list:" + imageNameList);
                Log.d("ABCDE", "this is fetch model list:" + modelNameList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ABCDE", "this database error:" + error.getDetails());
                Toast.makeText(MainActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDatabase() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference updateRef = firebaseDatabase.getReference("lUFMH5URcVPxCfSVKNRJopdXoy23");
        imageNameString = fileName.getText().toString().trim();
        String fileNameText = fileName.getText().toString().trim();
        imageNameList.add(fileNameText);
        modelNameList.add(fileNameText);
        ObjectFile objectFile = new ObjectFile(imageNameList, modelNameList);

        updateRef.setValue(objectFile);
        Log.d("ABCDE", "this is updated image list:" + imageNameList);
        Log.d("ABCDE", "this is updated model list:" + modelNameList);
    }

    private void updateStorage(String databaseLocation, String fileType) {
        storageReference = firebaseStorage.getReference(databaseLocation);
        if (filePath != null) {
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference ref;
            if (fileType.equals("image")) {
                ref = storageReference.child(imageNameString);
            } else {
                ref = storageReference.child(imageNameString + ".glb");
            }

            ref.putFile(filePath)
                    .addOnSuccessListener(
                            taskSnapshot -> {
                                progressDialog.dismiss();
                                Toast.makeText(MainActivity.this, "File Uploaded!!", Toast.LENGTH_SHORT).show();
                            })

                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("ABCDE", e.getLocalizedMessage());
                    })
                    .addOnProgressListener(
                            taskSnapshot -> {
                                double progress
                                        = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                progressDialog.setMessage("Uploaded " + (int) progress + "%");
                            })
            ;
        }
    }

    private void setUpView() {
        fileName = findViewById(R.id.fileNameET);
        selectImage = findViewById(R.id.imageSelect);
        selectGlb = findViewById(R.id.glbSelect);
        uploadImage = findViewById(R.id.imageUpload);
        uploadGlb = findViewById(R.id.glbbUpload);
        fileNameText = findViewById(R.id.fileNameTV);
        fetchDatabase = findViewById(R.id.fetchDatabase);
        updateDatabase = findViewById(R.id.updateDatabase);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

        }
    }

}