package com.wong.elapp.ui.home.second;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wong.elapp.R;

import java.util.ArrayList;
import java.util.List;

public class RecyAdapt extends RecyclerView.Adapter<RecyAdapt.MyHolder> {
    List<String> meanslist;
    public RecyAdapt(List<String> means) {
        meanslist = means;
    }

    @NonNull
    @Override
    public RecyAdapt.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_singleword_recycleitem,parent,false);
        return new MyHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyAdapt.MyHolder holder, int position) {
        holder.textView.setText(meanslist.get(position));
    }

    @Override
    public int getItemCount() {
        return meanslist.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView textView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.worditem);
        }
    }
}
