package com.android.artrial2.fragments.introduction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.android.artrial2.HomeActivity;
import com.android.artrial2.R;

public class IntroFragment3 extends Fragment {

    public IntroFragment3() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_introragment3, container, false);
        Button back = view.findViewById(R.id.btnIntroThreeBack);
        Button start = view.findViewById(R.id.btnIntroThreeStart);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        back.setOnClickListener(view1 -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.introductionFrame, new IntroFragment2())
                    .commit();
        });
        start.setOnClickListener(view12 -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("opened", true);
            editor.commit();
            startActivity(new Intent(getActivity(), HomeActivity.class));
            getActivity().finish();
        });
        // Inflate the layout for this fragment
        return view;
    }
}