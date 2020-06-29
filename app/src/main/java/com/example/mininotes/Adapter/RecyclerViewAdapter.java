package com.example.mininotes.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mininotes.Model.Model;
import com.example.mininotes.R;
import com.example.mininotes.Update;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Filterable {
    ArrayList<Model> arrayList;
    ArrayList<Model> filteredList;
    private Context context1;
    boolean isDark = false;

    public RecyclerViewAdapter(ArrayList<Model> arrayList, Context context1, boolean isDark) {
        this.arrayList = arrayList;
        this.context1 = context1;
        this.isDark = isDark;
        this.filteredList = arrayList;
    }

    public RecyclerViewAdapter(ArrayList<Model> arrayList, Context context1) {
        this.arrayList = arrayList;
        this.context1 = context1;
        this.filteredList = arrayList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_4_recyclerview, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        //Animation to RecyclerView
        holder.linearLayout.setAnimation(AnimationUtils.loadAnimation(context1, R.anim.teial));

        final Model model = filteredList.get(position);
        holder.note.setText(model.getNote());
        holder.title.setText(model.getTitle());
        holder.time.setText(model.getTime());


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(context1,"Helo"+model.getIndex(),Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putSerializable("All", model);
                Intent intent = new Intent(context1, Update.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtras(bundle);
                context1.startActivity(intent);


            }
        });


    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public Model getPosition(int position) {
        return arrayList.get(position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String key = constraint.toString();
                if (key.isEmpty()) {
                    filteredList = arrayList;
                } else {
                    ArrayList<Model> isFiltered = new ArrayList<>();
                    for (Model row : arrayList) {
                        if (row.getTitle().toLowerCase().contains(key.toLowerCase()) || row.getNote().toLowerCase().contains(key.toLowerCase())) {
                            isFiltered.add(row);
                        }
                    }
                    filteredList = isFiltered;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<Model>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, note, time;
        private LinearLayout linearLayout;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.note_title);
            note = itemView.findViewById(R.id.note);
            time = itemView.findViewById(R.id.time);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            cardView = itemView.findViewById(R.id.cardview);
            if (isDark) {
                setDark();
            }
        }

        public void setDark() {
            cardView.setBackgroundResource(R.drawable.border_bg_2);
        }
    }
}
