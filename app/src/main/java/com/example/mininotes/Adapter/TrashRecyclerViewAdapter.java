package com.example.mininotes.Adapter;


import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.telecom.InCallService;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mininotes.Model.Model;
import com.example.mininotes.Model.TrashModel;
import com.example.mininotes.R;
import com.example.mininotes.TrashActivity;
import com.example.mininotes.TrashDetailsActivity;
import com.example.mininotes.Update;

import java.util.ArrayList;

public class TrashRecyclerViewAdapter extends RecyclerView.Adapter<TrashRecyclerViewAdapter.ViewHolder1> {
    ArrayList<TrashModel> arrayList;
     Context context2;
    TrashActivity trashActivity;

    public TrashRecyclerViewAdapter(ArrayList<TrashModel> arrayList, Context context2) {
        this.arrayList = arrayList;
        this.context2 = context2;
        trashActivity= (TrashActivity) context2;

    }


    @NonNull
    @Override
    public TrashRecyclerViewAdapter.ViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context2).inflate(R.layout.model_4_recyclerview, parent, false);

        return new ViewHolder1(view,trashActivity);
    }

    @Override
    public void onBindViewHolder(@NonNull TrashRecyclerViewAdapter.ViewHolder1 holder, int position) {
        holder.linearLayout.setAnimation(AnimationUtils.loadAnimation(context2, R.anim.fade_scale));
        final TrashModel trashModel = arrayList.get(position);
        holder.note.setText(trashModel.getNote());
        holder.title.setText(trashModel.getTitle());
        holder.time.setText(trashModel.getTime());


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Bundle bundle = new Bundle();
                bundle.putSerializable("All", trashModel);
                Intent intent = new Intent(context2, TrashDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtras(bundle);
                context2.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder1 extends RecyclerView.ViewHolder {
        TextView title, note, time;
        private LinearLayout linearLayout;
        TrashActivity trashActivity;
        private CardView cardView;
        CheckBox checkBox;

        public ViewHolder1(@NonNull View itemView, TrashActivity trashActivity) {
            super(itemView);
            this.trashActivity = trashActivity;
            title = itemView.findViewById(R.id.note_title);
            note = itemView.findViewById(R.id.note);
            time = itemView.findViewById(R.id.time);
            cardView = itemView.findViewById(R.id.cardview);
            linearLayout = itemView.findViewById(R.id.linearLayout);


            linearLayout.setOnLongClickListener(trashActivity);

        }
    }
}
