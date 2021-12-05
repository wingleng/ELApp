package com.wong.elapp.ui.home.second;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wong.elapp.R;

import java.util.ArrayList;
import java.util.List;

public class RecyAdapt extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public final int CHINESE_MEAN = 1;
    public final int DERIVE_TITLE = 2;

    // TODO :这个构造函数有待改进

    List<String> mix_list;
    int size_of_meanslist;
    int size_of_derive;
    public RecyAdapt(List<String> means,List<String> derives) {
        means.add(0,"中文释义:");
        derives.add(0,"派生词:");
        size_of_meanslist = means.size();
        size_of_derive = derives.size();
        mix_list = new ArrayList<>();
        mix_list.addAll(means);
        mix_list.addAll(derives);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //标题“中文释义”的布局格式
        if (viewType == CHINESE_MEAN){
            View titleview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_singleword_recycleitem_title,parent,false);
            return new MeansTitleHolder(titleview);
        }
        //标题“派生词”的布局格式（实际上和上面的中文释义都是同一种布局，只是为了方便以后出现修改不同样式的情况，所以才写了两个不同的情况)
        else if (viewType == DERIVE_TITLE){
            View titleview2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_singleword_recycleitem_title,parent,false);
            return new DeriveTitleHoler(titleview2);
        }
        //默认的单词的布局格式
        else {
            View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_singleword_recycleitem, parent, false);
            return new MyHolder(itemview);
        }
    }


    /**
     * 在该方法中，分别获取不同的布局处理器，对布局中的组件进行设置。
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MeansTitleHolder){
            MeansTitleHolder viewHolder = (MeansTitleHolder) holder;
            viewHolder.titleTextView.setText(mix_list.get(position));
            viewHolder.titleTextView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//设置字体加粗
        }

        else if (holder instanceof DeriveTitleHoler){
            DeriveTitleHoler viewHoler = (DeriveTitleHoler) holder;
            viewHoler.titleTextView.setText(mix_list.get(position));
            viewHoler.titleTextView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//设置字体加粗
        }
        else if (holder instanceof MyHolder){
            MyHolder viewHolder = (MyHolder) holder;
            viewHolder.textView.setText(mix_list.get(position));
        }
    }


    @Override
    public int getItemCount() {
        return mix_list.size();
    }

    //这个方法用来分辨数据中的标题
    @Override
    public int getItemViewType(int position) {
        if (position == 0 ){
            return CHINESE_MEAN;
        }
        if (position == size_of_meanslist ) {
            return DERIVE_TITLE;
        }
        return super.getItemViewType(position);
    }

    /**
     * 默认单词的布局处理器
     */
    class MyHolder extends RecyclerView.ViewHolder{
        TextView textView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.worditem);
        }
    }

    /**
     * “中文释义”标题的布局处理器
     */
    class MeansTitleHolder extends RecyclerView.ViewHolder{
        TextView titleTextView;
        public MeansTitleHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.worditemtitle);
        }
    }

    /**
     * “派生词”标题的布局处理器
     */
    class DeriveTitleHoler extends RecyclerView.ViewHolder{
        TextView titleTextView;
        public DeriveTitleHoler(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.worditemtitle);
        }
    }
}
