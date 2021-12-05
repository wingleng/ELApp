package com.wong.elapp.ui.home.second;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.wong.elapp.R;
import com.wong.elapp.pojo.RandomList;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.MyViewHolder> {
    private List<RandomList> mlist;
    Context context;
    int cur_position;
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
        context = parent.getContext();

        //为TextView2（即单词）设置点击事件，点击播放其读音
        TextView textView2 = itemView.findViewById(R.id.textView2);
        textView2.setOnClickListener(new WordOnClickListener());

        return new MyViewHolder(itemView);
    }

    //这个方法负责设置组件
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        cur_position = holder.getAdapterPosition();//将当前的位置传递出去。。。。主要是为什么这个方法里面不能设置事件监听器啊。。
        holder.mtextView.setText(mlist.get(position).getNames());//设置单词
        setGlide(holder,position);//设置图片

        //设置卡片中的RecycleView，为其设置适配器。
        holder.wordlist.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));//设置好布局方向
        List<String> means = mlist.get(position).getMeans()== null ? new ArrayList<>() : mlist.get(position).getMeans();//数据处理，防止为空
        List<String> derives = mlist.get(position).getDerive()==null ? new ArrayList<>() : mlist.get(position).getDerive();

        Log.i("中文释义",""+mlist.get(position).getMeans());
        Log.i("派生",""+mlist.get(position).getDerive());
        holder.wordlist.setAdapter(new RecyAdapt(means,derives));
    }


    @Override
    public int getItemCount() {
        return mlist.size();
    }


    //TODO:这里以后可能要实现一个viewPager2，将图片更换掉，这么多张的图片，只显示一张真的太可惜了。
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView mtextView;
        QMUIRadiusImageView imageView;
        RecyclerView wordlist;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mtextView = itemView.findViewById(R.id.textView2);//标题
            imageView = itemView.findViewById(R.id.img1);//图片
            wordlist = itemView.findViewById(R.id.wordlist);//recycleview
        }
    }


    //TODO:以后这里要升级为ViewPager
    //设置图片，暂时的，以后会将图片区域换成ViewPager
    void setGlide(MyViewHolder holder,int position){
        int example_num = mlist.get(position).getExamples().size();
        if (example_num>0){
            Glide.with(context)
                    .load(mlist.get(position).getExamples().get(0).getImg())
                    .into(holder.imageView);
        }
        if (example_num==0){
            Glide.with(context)
                    .load(R.drawable.loading)
                    .into(holder.imageView);
        }
    }

    /**
     * 单词的点击监听器，用来播放音频，或者设置一些动画效果
     */
    class WordOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {

        }
    }
}
