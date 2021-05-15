package com.android.artrial2.fragments.introduction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.artrial2.HomeActivity;
import com.android.artrial2.R;

public class IntroFragment1 extends Fragment {

    public IntroFragment1() {
        // Required empty public constructor
    }
    private static final String SHARED = "shared";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_introragment1, container, false);

        TextView skip = view.findViewById(R.id.introOneSkip);
        Button next = view.findViewById(R.id.btnIntroOneNext);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        skip.setOnClickListener(view1 -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("opened", true);
            editor.commit();
//            Boolean opened = sharedPreferences.getBoolean("opened", false);
            startActivity(new Intent(getActivity(), HomeActivity.class));
            getActivity().finish();
        });
        next.setOnClickListener(view1 -> getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.introductionFrame, new IntroFragment2())
                .commit());

        /*Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Second fragment after 5 seconds appears
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.introductionFrame, new IntroFragment2())
                        .addToBackStack(null)
                        .commit();
            }
        }, Constants.DELAY);*/

        return view;
    }
    private void navigateFragment(){

    }
}