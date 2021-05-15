package com.android.artrial2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.artrial2.entities.ArAdapter;
import com.android.artrial2.entities.ArModelCard;
import com.android.artrial2.entities.ObjectFile;
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

public class HomeActivity extends AppCompatActivity {
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> imageNameList = new ArrayList<>();
    private ArrayList<String> imageUrlList = new ArrayList<>();
    private RecyclerView homeRecyclerView;
    private ArrayList<ArModelCard> arModelCards;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private android.widget.SearchView searchHeritage;
    private ArAdapter arAdapter;
    private MKLoader homeLoader;

    private void showInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, 0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = this.getSharedPreferences("myPref", Context.MODE_PRIVATE);
        Boolean opened = preferences.getBoolean("opened", false);
        if (!opened) {
            startActivity(new Intent(this, IntroductionActivity.class));
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        firebaseStorage = FirebaseStorage.getInstance();
//        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.homeProgressBar);
        homeRecyclerView = findViewById(R.id.cardRecyclerView);
        searchHeritage = findViewById(R.id.homeSearchView);
        homeLoader = findViewById(R.id.homeLoader);
        homeLoader.setVisibility(View.VISIBLE);
        findViewById(R.id.homeNavigation).setOnClickListener(view -> {
            startActivity(new Intent(this, AdminActivity.class));
        });
//        progressBar.setVisibility(View.VISIBLE);
        searchHeritage.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
//                imageNameList.contains(s)
                /*ArrayList<String> filter = new ArrayList<String>();
                if(imageNameList.contains(s)){
                    filter.add(s);
                    imageNameList = filter;
                }*/
                Log.d("ABCDEF", "this is submit text:" + imageNameList.contains(s));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

//                List<String> filtered = imageNameList.contains(s);
                Log.d("ABCDEF", "this is changing text:" + s);
                return false;
            }
        });
        searchHeritage.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    showInputMethod(view.findFocus());
                }
            }
        });

        searchHeritage.setSubmitButtonEnabled(true);
        searchHeritage.setQueryHint("Search Heritage");



      /*  if (firebaseAuth.getInstance().getCurrentUser() == null) {
            String email = "abcde@gmail.com";
            String password = "MiniProject123";
            firebaseUser(email, password);
        } else {
            Log.d("ABCDEF", "firebase registered");
        }*/

        FireabaseAsyncCard fireabaseAsyncCard = new FireabaseAsyncCard();
        fireabaseAsyncCard.execute();
    }


    private void setUpCards(ArrayList<String> imageNameList, ArrayList<String> imageUrlList) {
        arModelCards = new ArrayList<>();
//        arModelCards = finalCardList;
//        progressDialog.hide();

        if (imageNameList.size() > 0) {
            for (int i = 0; i < imageNameList.size(); i++) {
                String imageName = imageNameList.get(i);
                String imageUrl = imageUrlList.get(i);
                arModelCards.add(new ArModelCard(imageName, imageUrl, i));
            }
            arAdapter = new ArAdapter(this, arModelCards);
            int count = arAdapter.getItemCount();
            if (count > 1) {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
                homeRecyclerView.setLayoutManager(gridLayoutManager);
            } else {
                layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                homeRecyclerView.setLayoutManager(layoutManager);
            }
            homeLoader.setVisibility(View.INVISIBLE);
            homeRecyclerView.setAdapter(arAdapter);
        }
    }

    private class FireabaseAsyncCard extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            homeLoader.setVisibility(View.VISIBLE);
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
                    setUpCards(imageNameList, imageUrlList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("ABCDE", "this database error:" + error.getDetails());
                    Toast.makeText(HomeActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
            Log.d("ABCDEF", "trying something...");
            return true;
        }
    }

}

/*

    private void firebaseUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).
                addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.INVISIBLE);
//                                                        Toast.makeText(HomeActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(HomeActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                )
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(HomeActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                });

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.INVISIBLE);
//                            Toast.makeText(HomeActivity.this, "Registration successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(HomeActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e.getLocalizedMessage().contains("email address already in use")) {
                            Toast.makeText(HomeActivity.this, "Account already created", Toast.LENGTH_SHORT).show();
                            Toast.makeText(HomeActivity.this, "Signing In...", Toast.LENGTH_SHORT).show();
                            firebaseAuth.signInWithEmailAndPassword(email, password).
                                    addOnCompleteListener(
                                            new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        progressBar.setVisibility(View.INVISIBLE);
//                                                        Toast.makeText(HomeActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(HomeActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                    )
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(HomeActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });

    }

*/

