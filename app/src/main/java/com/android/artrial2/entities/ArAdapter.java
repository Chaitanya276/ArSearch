package com.android.artrial2.entities;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.artrial2.ArSceneView;
import com.android.artrial2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ArAdapter extends RecyclerView.Adapter<ArAdapter.Viewholder> {
    private Context context;
    private ArrayList<ArModelCard> arModelArrayList;

    // Constructor
    public ArAdapter(Context context, ArrayList<ArModelCard> arModelArrayList) {
        this.context = context;
        this.arModelArrayList = arModelArrayList;
    }


    @NonNull
    @Override
    public ArAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArAdapter.Viewholder holder, int position) {
        Log.d("ABCDE", "this is position ---> " + position);
        ArModelCard model = arModelArrayList.get(position);
        holder.heritageName.setText(model.getHeritageName());
        Picasso.get().load(model.getHeritageImage()).into(holder.heritageImage);
        Log.d("ABCDEF", "image url inside adapter" + model.getHeritageImage());

    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return arModelArrayList.size();
    }

    // View holder class for initializing of
    // your views such as TextView and Imageview.
    public class Viewholder extends RecyclerView.ViewHolder {
        private ImageView heritageImage;
        //        private ImageView heritageImage1;
        private TextView heritageName;
        //        private TextView heritageName1;
        private CardView cardView;

        //        private CardView cardView1;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            heritageName = itemView.findViewById(R.id.cardHeritageName);
            heritageImage = itemView.findViewById(R.id.cardHeritageImage);
//            heritageName1 = itemView.findViewById(R.id.cardHeritageName1);
//            heritageImage1 = itemView.findViewById(R.id.cardHeritageImage1);
            cardView = itemView.findViewById(R.id.arCardView);
            cardView.setOnClickListener(view -> {
//                final int position = (int) itemView.getTag();
                String cardName = "";
                String[] completeName = heritageName.getText().toString().split(" ");
                for(String a: completeName){
                    cardName += a;
                }
                cardName = cardName.toLowerCase();
                Intent it = new Intent(view.getContext(), ArSceneView.class);
                it.putExtra("heritageName", cardName.toLowerCase());
//                it.putExtra("modelName1", heritageName1.getText().toString().trim());
                context.startActivity(it);
            });

        }

    }
}
