package com.android.artrial2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.artrial2.fragments.introduction.IntroFragment1;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class IntroductionActivity extends AppCompatActivity {

    private FloatingActionButton nextIntroduction;
    private FloatingActionButton previousIntroduction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        FragmentManager supportFragmentWrapper = this.getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = supportFragmentWrapper.beginTransaction();
        fragmentTransaction.replace(R.id.introductionFrame, new IntroFragment1());

        fragmentTransaction.commit();

    }
}