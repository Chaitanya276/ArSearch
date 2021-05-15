package com.android.artrial2;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class ArSceneView extends AppCompatActivity {
    private ModelRenderable renderable;
    private ProgressBar circularProgressIndicator;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar_scene_view);
        String heritageName = getIntent().getStringExtra("heritageName");
        ArFragment arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
        progressDialog = new ProgressDialog(ArSceneView.this);
        progressDialog.setMessage("Loading model...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        FirebaseApp.initializeApp(this);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference("models/").child(heritageName + ".glb");

        if (heritageName != null) {
            try {
                File threeDModel = File.createTempFile(heritageName, "glb");


                storageReference.getFile(threeDModel).addOnSuccessListener(taskSnapshot -> {
                    RenderableSource renderableSource = RenderableSource
                            .builder()
                            .setSource(ArSceneView.this, Uri.parse(threeDModel.getPath()), RenderableSource.SourceType.GLB)
                            .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                            .build();

                    ModelRenderable
                            .builder()
                            .setSource(ArSceneView.this, renderableSource
                            )
                            .setRegistryId(threeDModel.getPath())
                            .build()
                            .thenAccept(modelRenderable -> {
                                Toast.makeText(ArSceneView.this, "Model built", Toast.LENGTH_SHORT).show();
                                renderable = modelRenderable;
                            })
                            .exceptionally(e -> {
                                Toast.makeText(ArSceneView.this, "Ar Scene exception", Toast.LENGTH_SHORT).show();
                                return null;
                            });
                    progressDialog.hide();

                })
                        .addOnFailureListener(e -> {
                            Toast.makeText(ArSceneView.this, "Model not found", Toast.LENGTH_SHORT).show();
                            progressDialog.hide();
                            Log.d("ABCDE", "this is failure:" + e.getLocalizedMessage());

                        });
            } catch (IOException e) {
                progressDialog.hide();
                Toast.makeText(ArSceneView.this, "something went wrong.", Toast.LENGTH_SHORT).show();

                Log.d("ABCDE", "this is exception:" + e.getLocalizedMessage());
//                circularProgressIndicator.setVisibility(View.INVISIBLE);
                e.printStackTrace();

            }
        } else {
            Toast.makeText(ArSceneView.this, "Heritage not found!", Toast.LENGTH_SHORT).show();
        }

        arFragment.setOnTapArPlaneListener(((hitResult, plane, motionEvent) -> {
            AnchorNode anchor = new AnchorNode(hitResult.createAnchor());
            TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());
            transformableNode.setLocalRotation(Quaternion.axisAngle(new Vector3(1f, 0, 0), 0f));
            transformableNode.setParent(anchor);
            transformableNode.setRenderable(renderable);
            arFragment.getArSceneView().getScene().addChild(anchor);
//            transformableNode.getScaleController().setMaxScale(0.02f);
//            transformableNode.getScaleController().setMinScale(0.01f);
//
//            transformableNode.setRenderable(renderable);
            transformableNode.select();


        }));
    }

}