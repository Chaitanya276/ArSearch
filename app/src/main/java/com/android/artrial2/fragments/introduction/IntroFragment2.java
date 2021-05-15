package com.android.artrial2.fragments.introduction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.artrial2.HomeActivity;
import com.android.artrial2.R;
import com.android.artrial2.entities.Constants;


public class IntroFragment2 extends Fragment {

    public IntroFragment2() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_introragment2, container, false);
        TextView skip = view.findViewById(R.id.introTwoSkip);
        Button next = view.findViewById(R.id.btnIntroTwoNext);
        Button back = view.findViewById(R.id.btnIntroTwoBack);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        skip.setOnClickListener(view1 -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("opened", true);
            editor.commit();
            startActivity(new Intent(getActivity(), HomeActivity.class));
            getActivity().finish();
        });
        back.setOnClickListener(view12 -> getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.introductionFrame, new IntroFragment1())
                .commit());
        next.setOnClickListener(view12 -> getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.introductionFrame, new IntroFragment3())
                .commit());
       /* Bitmap bitmap = Blurry.with(getActivity())
                .radius(10)
                .sampling(8)
                .capture(getView().findViewById(R.id.btnIntroOneNext)).get();

         ImageView img = getActivity().findViewById(R.id.btnIntroTwoNext);*/
//    (BitmapDrawable(R.drawable.transparent, bitmap));
//        img.setImageDrawable(new BitmapDrawable(bitmap));
        // Inflate the layout for this fragment
       /* Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //Second fragment after 5 seconds appears
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.introductionFrame, new IntroFragment3())
                        .addToBackStack(null)
                        .commit();
            }
        };*/
//        handler.postDelayed(runnable, Constants.DELAY);
        return view;
    }
}