package com.wong.elapp.ui.home.second;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wong.elapp.R;
import com.wong.elapp.pojo.RandomList;

import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.MyViewHolder> {
    private List<RandomList> mlist;
    public ViewPagerAdapter(List<RandomList> list) {
        mlist = list;
    }

    /**
     * 看网上的做法，貌似是在这个onCreateViewHolder方法中，对item内部的控件添加时间监听。。
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_singleword,parent,false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.mtextView.setText(mlist.get(position).getNames());
    }



    @Override
    public int getItemCount() {
        return mlist.size();
    }




    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView mtextView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mtextView = itemView.findViewById(R.id.textView2);
        }
    }
}
