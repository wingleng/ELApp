package com.wong.elapp.ui.dashboard.childrens;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wong.elapp.R;
import com.wong.elapp.ui.home.second.RecyAdapt;

import java.util.List;

public class ExplainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<String> explains;
    public ExplainAdapter(){}
    public ExplainAdapter(List<String> explains) {
        this.explains = explains;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_singleword_recycleitem, parent, false);

        return new MyHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myholder = (MyHolder) holder;
        myholder.textView.setText(explains.get(position));
    }

    @Override
    public int getItemCount() {
        return explains.size();
    }
    class MyHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.worditem);
        }
    }

    public List<String> getExplains() {
        return explains;
    }

    public void setExplains(List<String> explains) {
        this.explains = explains;
    }
}
