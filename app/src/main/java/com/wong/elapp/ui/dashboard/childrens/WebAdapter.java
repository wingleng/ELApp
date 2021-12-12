package com.wong.elapp.ui.dashboard.childrens;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wong.elapp.R;
import com.wong.elapp.pojo.vo.Youdaoresult.Web;

import java.util.List;

public class WebAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<Web> webs;

    public WebAdapter(List<Web> webs) {
        this.webs = webs;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_singleword_recycleitem, parent, false);

        return new MyHolder(itemview);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.textView.setText(webs.get(position).getKey()+"\n"+webs.get(position).getValue());
    }

    @Override
    public int getItemCount() {
        return webs.size();
    }
    class MyHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.worditem);
        }
    }
}
